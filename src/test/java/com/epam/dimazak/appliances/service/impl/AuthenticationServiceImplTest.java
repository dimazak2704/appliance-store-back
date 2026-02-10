package com.epam.dimazak.appliances.service.impl;

import com.epam.dimazak.appliances.config.security.GoogleTokenVerifier;
import com.epam.dimazak.appliances.config.security.JWTUtil;
import com.epam.dimazak.appliances.exception.UserAlreadyExistsException;
import com.epam.dimazak.appliances.model.Client;
import com.epam.dimazak.appliances.model.Role;
import com.epam.dimazak.appliances.model.VerificationToken;
import com.epam.dimazak.appliances.model.dto.auth.AuthRequest;
import com.epam.dimazak.appliances.model.dto.auth.AuthResponse;
import com.epam.dimazak.appliances.model.dto.auth.RegisterRequest;
import com.epam.dimazak.appliances.repository.ClientRepository;
import com.epam.dimazak.appliances.repository.EmployeeRepository;
import com.epam.dimazak.appliances.repository.VerificationTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JWTUtil jwtUtil;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private VerificationTokenRepository tokenRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private GoogleTokenVerifier googleTokenVerifier;
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private AuthenticationServiceImpl authService;

    @Test
    void register_whenUserExists_throwsException() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");

        when(clientRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new Client()));
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Exists");

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("Exists");
    }

    @Test
    void register_whenNewUser_savesClientAndSendsEmail() {
        RegisterRequest request = new RegisterRequest();
        request.setName("John");
        request.setEmail("john@example.com");
        request.setPassword("pass");
        request.setCard("1234567890123456");

        when(clientRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(employeeRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");

        AuthResponse response = authService.register(request);

        verify(clientRepository).save(any(Client.class));
        verify(tokenRepository).save(any(VerificationToken.class));
        verify(emailService).sendSimpleMessage(eq(request.getEmail()), any(), any());
        assertThat(response.getRole()).isEqualTo("GUEST");
    }

    @Test
    void authenticate_whenCredentialsValid_returnsToken() {
        AuthRequest request = new AuthRequest();
        request.setEmail("john@example.com");
        request.setPassword("pass");

        Client client = new Client();
        client.setEmail("john@example.com");
        client.setName("John");
        client.setRole(Role.CLIENT);

        when(clientRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(client));
        when(jwtUtil.generateToken(any(), eq(client))).thenReturn("tokens123");

        AuthResponse response = authService.authenticate(request);

        assertThat(response.getToken()).isEqualTo("tokens123");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void authenticate_whenUserNotFound_throwsBadCredentials() {
        AuthRequest request = new AuthRequest();
        request.setEmail("unknown@example.com");
        request.setPassword("pass");

        when(clientRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(employeeRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Bad credentials");

        assertThatThrownBy(() -> authService.authenticate(request))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Bad credentials");
    }
}
