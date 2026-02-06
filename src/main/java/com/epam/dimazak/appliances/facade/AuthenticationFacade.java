package com.epam.dimazak.appliances.facade;

import com.epam.dimazak.appliances.model.dto.auth.*;
import com.epam.dimazak.appliances.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationFacade {

    private final AuthenticationService authenticationService;

    public AuthResponse register(com.epam.dimazak.appliances.model.dto.auth.@Valid RegisterRequest request) {
        return authenticationService.register(request);
    }

    public AuthResponse authenticate(AuthRequest request) {
        return authenticationService.authenticate(request);
    }

    public void confirmEmail(String token) {
        authenticationService.confirmEmail(token);
    }

    public void forgotPassword(String email) {
        authenticationService.forgotPassword(email);
    }

    public void resetPassword(PasswordResetRequest request) {
        authenticationService.resetPassword(request);
    }

    public AuthResponse registerWithGoogle(GoogleRegisterRequest request) {
        return authenticationService.registerWithGoogle(request);
    }

    public AuthResponse loginWithGoogle(GoogleLoginRequest request){
        return authenticationService.loginWithGoogle(request);
    }
}