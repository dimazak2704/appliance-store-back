package com.epam.dimazak.appliances.controller;

import com.epam.dimazak.appliances.model.dto.auth.*;
import com.epam.dimazak.appliances.facade.AuthenticationFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationFacade authenticationFacade;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(authenticationFacade.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody @Valid AuthRequest request) {
        return ResponseEntity.ok(authenticationFacade.authenticate(request));
    }

    @PostMapping("/confirm-email")
    public ResponseEntity<Void> confirmEmail(@RequestParam String token) {
        authenticationFacade.confirmEmail(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestParam String email) {
        authenticationFacade.forgotPassword(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid PasswordResetRequest request) {
        authenticationFacade.resetPassword(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/google-register")
    public ResponseEntity<AuthResponse> registerWithGoogle(@RequestBody @Valid GoogleRegisterRequest request) {
        return ResponseEntity.ok(authenticationFacade.registerWithGoogle(request));
    }

    @PostMapping("/google-login")
    public ResponseEntity<AuthResponse> loginWithGoogle(@RequestBody @Valid GoogleLoginRequest request) {
        return ResponseEntity.ok(authenticationFacade.loginWithGoogle(request));
    }
}