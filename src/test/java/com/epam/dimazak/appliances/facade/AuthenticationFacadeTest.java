package com.epam.dimazak.appliances.facade;

import com.epam.dimazak.appliances.model.dto.auth.*;
import com.epam.dimazak.appliances.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationFacadeTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationFacade authenticationFacade;

    @Test
    void register_shouldCallService() {
        RegisterRequest request = new RegisterRequest();
        AuthResponse expectedResponse = new AuthResponse();
        when(authenticationService.register(request)).thenReturn(expectedResponse);

        AuthResponse result = authenticationFacade.register(request);

        assertThat(result).isEqualTo(expectedResponse);
        verify(authenticationService).register(request);
    }

    @Test
    void authenticate_shouldCallService() {
        AuthRequest request = new AuthRequest();
        AuthResponse expectedResponse = new AuthResponse();
        when(authenticationService.authenticate(request)).thenReturn(expectedResponse);

        AuthResponse result = authenticationFacade.authenticate(request);

        assertThat(result).isEqualTo(expectedResponse);
        verify(authenticationService).authenticate(request);
    }

    @Test
    void confirmEmail_shouldCallService() {
        String token = "token";
        authenticationFacade.confirmEmail(token);
        verify(authenticationService).confirmEmail(token);
    }

    @Test
    void forgotPassword_shouldCallService() {
        String email = "test@example.com";
        authenticationFacade.forgotPassword(email);
        verify(authenticationService).forgotPassword(email);
    }

    @Test
    void resetPassword_shouldCallService() {
        PasswordResetRequest request = new PasswordResetRequest();
        authenticationFacade.resetPassword(request);
        verify(authenticationService).resetPassword(request);
    }

    @Test
    void registerWithGoogle_shouldCallService() {
        GoogleRegisterRequest request = new GoogleRegisterRequest();
        AuthResponse expectedResponse = new AuthResponse();
        when(authenticationService.registerWithGoogle(request)).thenReturn(expectedResponse);

        AuthResponse result = authenticationFacade.registerWithGoogle(request);

        assertThat(result).isEqualTo(expectedResponse);
        verify(authenticationService).registerWithGoogle(request);
    }

    @Test
    void loginWithGoogle_shouldCallService() {
        GoogleLoginRequest request = new GoogleLoginRequest();
        AuthResponse expectedResponse = new AuthResponse();
        when(authenticationService.loginWithGoogle(request)).thenReturn(expectedResponse);

        AuthResponse result = authenticationFacade.loginWithGoogle(request);

        assertThat(result).isEqualTo(expectedResponse);
        verify(authenticationService).loginWithGoogle(request);
    }
}
