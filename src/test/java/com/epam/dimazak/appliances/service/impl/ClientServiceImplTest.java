package com.epam.dimazak.appliances.service.impl;

import com.epam.dimazak.appliances.exception.BusinessRuleException;
import com.epam.dimazak.appliances.exception.UserAlreadyExistsException;
import com.epam.dimazak.appliances.model.Client;
import com.epam.dimazak.appliances.model.Role;
import com.epam.dimazak.appliances.model.dto.admin.AdminUserDto;
import com.epam.dimazak.appliances.model.dto.admin.UserCreateRequest;
import com.epam.dimazak.appliances.model.dto.profile.ChangePasswordRequest;
import com.epam.dimazak.appliances.model.dto.profile.UpdateProfileRequest;
import com.epam.dimazak.appliances.model.dto.profile.СlientProfileDto;
import com.epam.dimazak.appliances.repository.ClientRepository;
import com.epam.dimazak.appliances.repository.EmployeeRepository;
import com.epam.dimazak.appliances.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    void getProfile_returnsClientProfile() {
        String email = "user@example.com";
        Client client = new Client();
        client.setEmail(email);
        client.setName("John");
        client.setCard("1234567890123456");
        client.setRole(Role.CLIENT);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(client));

        СlientProfileDto result = clientService.getProfile(email);

        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getName()).isEqualTo("John");
        assertThat(result.getCard()).isEqualTo("1234567890123456");
    }

    @Test
    void updateProfile_updatesFieldsSuccessfully() {
        String email = "user@example.com";
        Client client = new Client();
        client.setEmail(email);
        client.setName("Old Name");
        client.setRole(Role.CLIENT);

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setName("New Name");
        request.setCard("1234567890123456");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(client));
        when(userRepository.save(client)).thenReturn(client);

        clientService.updateProfile(email, request);

        assertThat(client.getName()).isEqualTo("New Name");
        assertThat(client.getCard()).isEqualTo("1234567890123456");
        verify(userRepository).save(client);
    }

    @Test
    void changePassword_whenCurrentPasswordCorrect_changesPassword() {
        String email = "user@example.com";
        Client client = new Client();
        client.setEmail(email);
        client.setPassword("oldHashedPassword");

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("oldPassword");
        request.setNewPassword("newPassword");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(client));
        when(passwordEncoder.matches("oldPassword", "oldHashedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("newHashedPassword");

        clientService.changePassword(email, request);

        assertThat(client.getPassword()).isEqualTo("newHashedPassword");
        verify(userRepository).save(client);
    }

    @Test
    void changePassword_whenCurrentPasswordIncorrect_throwsException() {
        String email = "user@example.com";
        Client client = new Client();
        client.setEmail(email);
        client.setPassword("oldHashedPassword");

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("wrongPassword");
        request.setNewPassword("newPassword");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(client));
        when(passwordEncoder.matches("wrongPassword", "oldHashedPassword")).thenReturn(false);
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Invalid password");

        assertThatThrownBy(() -> clientService.changePassword(email, request))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void createUser_whenEmailNotExists_createsSuccessfully() {
        UserCreateRequest request = new UserCreateRequest();
        request.setName("John");
        request.setEmail("john@example.com");
        request.setPassword("password");
        request.setCard("1234567890123456");
        request.setRole("CLIENT");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(Client.class))).thenAnswer(invocation -> {
            Client saved = invocation.getArgument(0);
            saved.setId(1L);
            saved.setRole(Role.CLIENT);
            return saved;
        });

        AdminUserDto result = clientService.createUser(request);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getEmail()).isEqualTo("john@example.com");
        verify(userRepository).save(any(Client.class));
    }

    @Test
    void createUser_whenEmailExists_throwsException() {
        UserCreateRequest request = new UserCreateRequest();
        request.setEmail("existing@example.com");

        Client existing = new Client();
        existing.setId(99L);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existing));
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("User exists");

        assertThatThrownBy(() -> clientService.createUser(request))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    void updateUserStatus_whenBanningAdmin_throwsException() {
        Long id = 1L;
        Client client = new Client();
        client.setId(id);
        client.setRole(Role.ADMIN);
        client.setEnabled(true);

        when(userRepository.findById(id)).thenReturn(Optional.of(client));
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Cannot ban an ADMIN");

        assertThatThrownBy(() -> clientService.updateUserStatus(id, false))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void updateUserStatus_whenBanningClient_updatesSuccessfully() {
        Long id = 1L;
        Client client = new Client();
        client.setId(id);
        client.setRole(Role.CLIENT);
        client.setEnabled(true);

        when(userRepository.findById(id)).thenReturn(Optional.of(client));

        clientService.updateUserStatus(id, false);

        assertThat(client.isEnabled()).isFalse();
        verify(userRepository).save(client);
    }
}
