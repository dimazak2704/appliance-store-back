package com.epam.dimazak.appliances.service.impl;

import com.epam.dimazak.appliances.aspect.Loggable;
import com.epam.dimazak.appliances.exception.BusinessRuleException;
import com.epam.dimazak.appliances.exception.ResourceNotFoundException;
import com.epam.dimazak.appliances.exception.UserAlreadyExistsException;
import com.epam.dimazak.appliances.model.*;
import com.epam.dimazak.appliances.model.dto.admin.*;
import com.epam.dimazak.appliances.model.dto.profile.*;
import com.epam.dimazak.appliances.repository.*;
import com.epam.dimazak.appliances.repository.specification.*;
import com.epam.dimazak.appliances.service.ClientService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    @PersistenceContext
    private EntityManager entityManager;

    private String getMsg(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMsg("error.credentials.invalid")));
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(getMsg("error.credentials.invalid")));
    }

    @Override
    @Loggable
    public СlientProfileDto getProfile(String email) {
        return mapToProfileDto(findUser(email));
    }

    @Override
    @Transactional
    @Loggable
    public СlientProfileDto updateProfile(String email, UpdateProfileRequest request) {
        User user = findUser(email);
        user.setName(request.getName());

        if (user instanceof Client client) {
            client.setCard(request.getCard());
        }

        return mapToProfileDto(userRepository.save(user));
    }

    @Override
    @Transactional
    @Loggable
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = findUser(email);
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException(getMsg("error.password.invalid"));
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public Page<AdminUserDto> getAllUsers(UserFilterDto filter, Pageable pageable) {
        return userRepository.findAll(UserSpecification.getSpecifications(filter), pageable)
                .map(this::mapToAdminDto);
    }

    @Override
    public AdminUserDto getUserById(Long id) {
        return mapToAdminDto(findUser(id));
    }

    @Override
    @Transactional
    @Loggable
    public AdminUserDto createUser(UserCreateRequest request) {
        validateEmailUniqueness(request.getEmail(), null);

        Role role = Role.valueOf(request.getRole());
        String pass = passwordEncoder.encode(request.getPassword());

        User user;
        if (role == Role.CLIENT) {
            user = new Client(null, request.getName(), request.getEmail(), pass, request.getCard(), true);
        } else {
            user = new Employee(null, request.getName(), request.getEmail(), pass, "General", true);
            user.setRole(role);
        }

        return mapToAdminDto(userRepository.save(user));
    }

    @Override
    @Transactional
    @Loggable
    public AdminUserDto updateUser(Long id, UserUpdateRequest request) {
        User user = findUser(id);
        validateEmailUniqueness(request.getEmail(), user);

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        if (user instanceof Client client) {
            client.setCard(request.getCard());
        }

        return mapToAdminDto(userRepository.save(user));
    }

    @Override
    @Transactional
    @Loggable
    public void updateUserRole(Long id, String roleName) {
        User user = findUser(id);
        Role newRole = Role.valueOf(roleName);

        if (user.getRole() == newRole) return;

        boolean isMovingToStaff = (newRole == Role.ADMIN || newRole == Role.EMPLOYEE);
        boolean isCurrentlyClient = user instanceof Client;

        if (isCurrentlyClient && isMovingToStaff) {
            entityManager.createNativeQuery("DELETE FROM client WHERE id = :id")
                    .setParameter("id", id)
                    .executeUpdate();

            entityManager.createNativeQuery("INSERT INTO employee (id, department) VALUES (:id, :dept)")
                    .setParameter("id", id)
                    .setParameter("dept", "General")
                    .executeUpdate();

        } else if (!isCurrentlyClient && newRole == Role.CLIENT) {
            entityManager.createNativeQuery("DELETE FROM employee WHERE id = :id")
                    .setParameter("id", id)
                    .executeUpdate();

            entityManager.createNativeQuery("INSERT INTO client (id, card) VALUES (:id, :card)")
                    .setParameter("id", id)
                    .setParameter("card", null)
                    .executeUpdate();
        }

        user.setRole(newRole);
        userRepository.save(user);

        entityManager.flush();
        entityManager.clear();
    }

    @Override
    @Transactional
    @Loggable
    public void updateUserStatus(Long id, boolean isEnabled) {
        User user = findUser(id);
        if (user.getRole() == Role.ADMIN && !isEnabled) {
            throw new BusinessRuleException(getMsg("error.user.ban_admin"));
        }
        user.setEnabled(isEnabled);
        userRepository.save(user);
    }

    @Override
    @Transactional
    @Loggable
    public void deleteUser(Long id) {
        User user = findUser(id);
        try {
            userRepository.delete(user);
        } catch (Exception e) {
            throw new BusinessRuleException(getMsg("error.user.delete_linked"));
        }
    }

    @Override
    @Transactional
    @Loggable
    public void adminResetPassword(Long id, String newPassword) {
        User user = findUser(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private void validateEmailUniqueness(String email, User currentUser) {
        userRepository.findByEmail(email).ifPresent(existingUser -> {
            if (currentUser == null || !existingUser.getId().equals(currentUser.getId())) {
                throw new UserAlreadyExistsException(getMsg("error.user.exists"));
            }
        });
    }

    private AdminUserDto mapToAdminDto(User user) {
        return AdminUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .card(user instanceof Client ? ((Client) user).getCard() : null)
                .isEnabled(user.isEnabled())
                .build();
    }

    private СlientProfileDto mapToProfileDto(User user) {
        return СlientProfileDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .card(user instanceof Client ? ((Client) user).getCard() : null)
                .build();
    }
}