package com.epam.dimazak.appliances.service;


import com.epam.dimazak.appliances.model.dto.auth.*;

public interface AuthenticationService {
    AuthResponse register(RegisterRequest request);
    AuthResponse authenticate(AuthRequest request);
    void confirmEmail(String token);
    void forgotPassword(String email);
    void resetPassword(PasswordResetRequest request);
    AuthResponse registerWithGoogle(GoogleRegisterRequest request);
    AuthResponse loginWithGoogle(GoogleLoginRequest request);
}