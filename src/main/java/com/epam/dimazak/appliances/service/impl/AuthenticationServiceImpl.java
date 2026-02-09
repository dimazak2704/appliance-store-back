package com.epam.dimazak.appliances.service.impl;

import com.epam.dimazak.appliances.aspect.Loggable;
import com.epam.dimazak.appliances.config.security.GoogleTokenVerifier;
import com.epam.dimazak.appliances.exception.InvalidTokenException;
import com.epam.dimazak.appliances.model.VerificationToken;
import com.epam.dimazak.appliances.model.dto.auth.*;
import com.epam.dimazak.appliances.exception.UserAlreadyExistsException;
import com.epam.dimazak.appliances.model.Client;
import com.epam.dimazak.appliances.model.User;
import com.epam.dimazak.appliances.repository.ClientRepository;
import com.epam.dimazak.appliances.repository.EmployeeRepository;
import com.epam.dimazak.appliances.config.security.JWTUtil;
import com.epam.dimazak.appliances.repository.VerificationTokenRepository;
import com.epam.dimazak.appliances.service.AuthenticationService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final VerificationTokenRepository tokenRepository;
    private final EmailService emailService;
    private final GoogleTokenVerifier googleTokenVerifier;
    private final MessageSource messageSource;

    private String getMsg(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    @Override
    @Transactional
    @Loggable
    public AuthResponse register(RegisterRequest request) {
        if (clientRepository.findByEmail(request.getEmail()).isPresent() ||
                employeeRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(getMsg("error.user.exists"));
        }

        var client = new Client(
                null, request.getName(), request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getCard(),
                false
        );
        clientRepository.save(client);

        VerificationToken token = new VerificationToken(
                client.getEmail(),
                VerificationToken.TokenType.EMAIL_VERIFICATION,
                24 * 60
        );
        tokenRepository.save(token);

        String link = "http://localhost:5173/confirm-email?token=" + token.getToken();
        emailService.sendEmail(
                client.getEmail(),
                "Confirm your email",
                "Click here to activate your account: " + link
        );

        return AuthResponse.builder().token(null).role("GUEST").build();
    }

    @Loggable
    @Override
    public void confirmEmail(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException(getMsg("error.token.invalid")));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException(getMsg("error.token.expired"));
        }

        String email = verificationToken.getEmail();

        clientRepository.findByEmail(email).ifPresent(client -> {
            client.setEnabled(true);
            clientRepository.save(client);
        });

        tokenRepository.delete(verificationToken);
    }

    @Loggable
    @Override
    public void forgotPassword(String email) {
        if (clientRepository.findByEmail(email).isEmpty() &&
                employeeRepository.findByEmail(email).isEmpty()) {
            return;
        }

        tokenRepository.deleteByEmailAndType(email, VerificationToken.TokenType.PASSWORD_RESET);

        VerificationToken token = new VerificationToken(
                email,
                VerificationToken.TokenType.PASSWORD_RESET,
                30
        );
        tokenRepository.save(token);

        String link = "http://localhost:5173/reset-password?token=" + token.getToken();
        emailService.sendEmail(
                email,
                "Reset Password",
                "Click here to reset password: " + link
        );
    }

    @Loggable
    @Override
    public void resetPassword(PasswordResetRequest request) {
        VerificationToken token = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new InvalidTokenException(getMsg("error.token.invalid")));

        if (token.getType() != VerificationToken.TokenType.PASSWORD_RESET) {
            throw new InvalidTokenException("Invalid token type");
        }

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException(getMsg("error.token.expired"));
        }

        String encodedPass = passwordEncoder.encode(request.getNewPassword());
        String email = token.getEmail();

        clientRepository.findByEmail(email).ifPresent(c -> {
            c.setPassword(encodedPass);
            clientRepository.save(c);
        });

        employeeRepository.findByEmail(email).ifPresent(e -> {
            e.setPassword(encodedPass);
            employeeRepository.save(e);
        });

        tokenRepository.delete(token);
    }

    @Loggable
    @Override
    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = clientRepository.findByEmail(request.getEmail())
                .map(u -> (User) u)
                .orElseGet(() -> employeeRepository.findByEmail(request.getEmail())
                        .orElseThrow(() -> new BadCredentialsException("error.credentials.invalid")));

        String token = jwtUtil.generateToken(Map.of("role", user.getRole().name()), user);

        return AuthResponse.builder()
                .token(token)
                .role(user.getRole().name())
                .name(user.getName()) 
                .build();
    }

    @Override
    @Transactional
    @Loggable
    public AuthResponse registerWithGoogle(GoogleRegisterRequest request) {
        var payload = googleTokenVerifier.verify(request.getToken());

        String email = payload.getEmail();
        String name = (String) payload.get("name");

        if (clientRepository.findByEmail(email).isPresent() ||
                employeeRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("error.user.exists");
        }

        var client = new Client(
                null,
                name,
                email,
                passwordEncoder.encode(request.getPassword()),
                request.getCard(),
                true
        );

        clientRepository.save(client);

        String jwtToken = jwtUtil.generateToken(Map.of("role", client.getRole().name()), client);

        return AuthResponse.builder()
                .token(jwtToken)
                .role(client.getRole().name())
                .name(client.getName())
                .build();
    }

    @Override
    @Loggable
    public AuthResponse loginWithGoogle(GoogleLoginRequest request) {
        var payload = googleTokenVerifier.verify(request.getToken());
        String email = payload.getEmail();

        User user = clientRepository.findByEmail(email)
                .map(u -> (User) u)
                .orElseGet(() -> employeeRepository.findByEmail(email)
                        .orElseThrow(() -> new BadCredentialsException("error.credentials.userNotRegistered")));

        String jwtToken = jwtUtil.generateToken(Map.of("role", user.getRole().name()), user);

        return AuthResponse.builder()
                .token(jwtToken)
                .role(user.getRole().name())
                .name(user.getName())
                .build();
    }
}