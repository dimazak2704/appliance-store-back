package com.epam.dimazak.appliances.service.impl;

import com.epam.dimazak.appliances.aspect.Loggable;
import com.epam.dimazak.appliances.exception.BusinessRuleException;
import com.epam.dimazak.appliances.exception.ResourceNotFoundException;
import com.epam.dimazak.appliances.exception.UserAlreadyExistsException;
import com.epam.dimazak.appliances.model.Client;
import com.epam.dimazak.appliances.model.Role;
import com.epam.dimazak.appliances.model.dto.admin.AdminUserDto;
import com.epam.dimazak.appliances.model.dto.admin.UserCreateRequest;
import com.epam.dimazak.appliances.model.dto.admin.UserFilterDto;
import com.epam.dimazak.appliances.model.dto.admin.UserUpdateRequest;
import com.epam.dimazak.appliances.model.dto.profile.ChangePasswordRequest;
import com.epam.dimazak.appliances.model.dto.profile.UpdateProfileRequest;
import com.epam.dimazak.appliances.model.dto.profile.СlientProfileDto;
import com.epam.dimazak.appliances.repository.ClientRepository;
import com.epam.dimazak.appliances.repository.specification.ClientSpecification;
import com.epam.dimazak.appliances.service.ClientService;
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

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    private String getMsg(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    @Override
    @Loggable
    public СlientProfileDto getProfile(String email) {
        Client client = getClient(email);
        return mapToDto(client);
    }

    @Override
    @Transactional
    @Loggable
    public СlientProfileDto updateProfile(String email, UpdateProfileRequest request) {
        Client client = getClient(email);
        client.setName(request.getName());
        client.setCard(request.getCard());
        clientRepository.save(client);
        return mapToDto(client);
    }

    @Override
    @Transactional
    @Loggable
    public void changePassword(String email, ChangePasswordRequest request) {
        Client client = getClient(email);

        if (!passwordEncoder.matches(request.getCurrentPassword(), client.getPassword())) {
            throw new BadCredentialsException(getMsg("error.password.invalid"));
        }

        client.setPassword(passwordEncoder.encode(request.getNewPassword()));
        clientRepository.save(client);
    }

    private Client getClient(String email) {
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(getMsg("error.credentials.invalid")));
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public Page<AdminUserDto> getAllUsers(UserFilterDto filter, Pageable pageable) {
        return clientRepository.findAll(ClientSpecification.getSpecifications(filter), pageable)
                .map(this::mapToAdminDto);
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public AdminUserDto getUserById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMsg("error.credentials.invalid")));
        return mapToAdminDto(client);
    }

    @Override
    @Transactional
    @Loggable
    public AdminUserDto createUser(UserCreateRequest request) {
        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(getMsg("error.user.exists"));
        }

        Client client = new Client();
        client.setName(request.getName());
        client.setEmail(request.getEmail());
        client.setPassword(passwordEncoder.encode(request.getPassword()));
        client.setCard(request.getCard());
        client.setEnabled(true);

        try {
            client.setRole(Role.valueOf(request.getRole()));
        } catch (IllegalArgumentException e) {
            throw new BusinessRuleException("Invalid role: " + request.getRole());
        }

        return mapToAdminDto(clientRepository.save(client));
    }

    @Override
    @Transactional
    @Loggable
    public AdminUserDto updateUser(Long id, UserUpdateRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMsg("error.credentials.invalid")));

        if (!client.getEmail().equals(request.getEmail()) && clientRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(getMsg("error.user.exists"));
        }

        client.setName(request.getName());
        client.setEmail(request.getEmail());
        client.setCard(request.getCard());

        return mapToAdminDto(clientRepository.save(client));
    }

    @Override
    @Transactional
    @Loggable
    public void updateUserRole(Long id, String roleName) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMsg("error.credentials.invalid")));
        try {
            client.setRole(Role.valueOf(roleName));
            clientRepository.save(client);
        } catch (IllegalArgumentException e) {
            throw new BusinessRuleException("Invalid role: " + roleName);
        }
    }

    @Override
    @Transactional
    @Loggable
    public void updateUserStatus(Long id, boolean isEnabled) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMsg("error.credentials.invalid")));

        if (client.getRole() == Role.ADMIN && !isEnabled) {
            throw new BusinessRuleException("Cannot ban an ADMIN.");
        }

        client.setEnabled(isEnabled);
        clientRepository.save(client);
    }

    @Override
    @Transactional
    @Loggable
    public void deleteUser(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException(getMsg("error.credentials.invalid"));
        }
        try {
            clientRepository.deleteById(id);
        } catch (Exception e) {
            throw new BusinessRuleException("Cannot delete user with existing orders. Please ban the user instead.");
        }
    }

    private AdminUserDto mapToAdminDto(Client client) {
        return AdminUserDto.builder()
                .id(client.getId())
                .name(client.getName())
                .email(client.getEmail())
                .role(client.getRole().name())
                .card(client.getCard())
                .isEnabled(client.isEnabled())
                .build();
    }

    private СlientProfileDto mapToDto(Client client) {
        return СlientProfileDto.builder()
                .name(client.getName())
                .email(client.getEmail())
                .card(client.getCard())
                .role(client.getRole().name())
                .build();
    }
}