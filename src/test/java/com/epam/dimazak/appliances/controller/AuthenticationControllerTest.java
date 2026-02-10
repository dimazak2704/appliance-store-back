package com.epam.dimazak.appliances.controller;

import com.epam.dimazak.appliances.facade.AuthenticationFacade;
import com.epam.dimazak.appliances.model.dto.auth.AuthRequest;
import com.epam.dimazak.appliances.model.dto.auth.AuthResponse;
import com.epam.dimazak.appliances.model.dto.auth.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationFacade authenticationFacade;

    private AuthenticationController controller;

    @BeforeEach
    void setUp() {
        controller = new AuthenticationController(authenticationFacade);
    }

    @Test
    void register_shouldReturnOkAndAuthResponse() {
        RegisterRequest request = new RegisterRequest();
        AuthResponse response = AuthResponse.builder().token("token").build();

        when(authenticationFacade.register(any(RegisterRequest.class))).thenReturn(response);

        ResponseEntity<AuthResponse> result = controller.register(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
        verify(authenticationFacade).register(request);
    }

    @Test
    void authenticate_shouldReturnOkAndAuthResponse() {
        AuthRequest request = new AuthRequest();
        AuthResponse response = AuthResponse.builder().token("token").build();

        when(authenticationFacade.authenticate(any(AuthRequest.class))).thenReturn(response);

        ResponseEntity<AuthResponse> result = controller.authenticate(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
        verify(authenticationFacade).authenticate(request);
    }

    @Test
    void confirmEmail_shouldReturnOk() {
        String token = "token";

        ResponseEntity<Void> result = controller.confirmEmail(token);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(authenticationFacade).confirmEmail(token);
    }
}
