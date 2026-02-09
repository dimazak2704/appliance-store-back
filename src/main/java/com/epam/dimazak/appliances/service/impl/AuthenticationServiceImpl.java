package com.epam.dimazak.appliances.service.impl;

import com.epam.dimazak.appliances.aspect.Loggable;
import com.epam.dimazak.appliances.config.security.GoogleTokenVerifier;
import com.epam.dimazak.appliances.config.security.JWTUtil;
import com.epam.dimazak.appliances.exception.InvalidTokenException;
import com.epam.dimazak.appliances.exception.UserAlreadyExistsException;
import com.epam.dimazak.appliances.model.Client;
import com.epam.dimazak.appliances.model.User;
import com.epam.dimazak.appliances.model.VerificationToken;
import com.epam.dimazak.appliances.model.dto.auth.*;
import com.epam.dimazak.appliances.repository.ClientRepository;
import com.epam.dimazak.appliances.repository.EmployeeRepository;
import com.epam.dimazak.appliances.repository.VerificationTokenRepository;
import com.epam.dimazak.appliances.service.AuthenticationService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
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

    private String getMsg(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
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
                null,
                request.getName(),
                request.getEmail(),
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

        String subject = getMsg("email.confirm.subject");
        String body = getMsg("email.confirm.body", link);

        emailService.sendSimpleMessage(client.getEmail(), subject, body);

        return AuthResponse.builder().token(null).role("GUEST").build();
    }

    @Loggable
    @Override
    @Transactional
    public void confirmEmail(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException(getMsg("error.token.invalid")));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException(getMsg("error.token.expired"));
        }

        String email = verificationToken.getEmail();

        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidTokenException(getMsg("error.user.not_found")));

        client.setEnabled(true);
        clientRepository.save(client);

        tokenRepository.delete(verificationToken);
    }

    @Loggable
    @Override
    @Transactional
    public void forgotPassword(String email) {
        if (clientRepository.findByEmail(email).isEmpty() &&
                employeeRepository.findByEmail(email).isEmpty()) {
            log.warn("Forgot password request for non-existent email: {}", email);
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

        String subject = getMsg("email.reset.subject");
        String body = getMsg("email.reset.body", link);

        emailService.sendSimpleMessage(email, subject, body);
    }

    @Loggable
    @Override
    @Transactional
    public void resetPassword(PasswordResetRequest request) {
        VerificationToken token = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new InvalidTokenException(getMsg("error.token.invalid")));

        if (token.getType() != VerificationToken.TokenType.PASSWORD_RESET) {
            throw new InvalidTokenException(getMsg("error.token.invalid_type"));
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
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            // Локалізуємо стандартну помилку Spring Security
            throw new BadCredentialsException(getMsg("error.credentials.invalid"));
        }

        User user = clientRepository.findByEmail(request.getEmail())
                .map(u -> (User) u)
                .orElseGet(() -> employeeRepository.findByEmail(request.getEmail())
                        .orElseThrow(() -> new BadCredentialsException(getMsg("error.credentials.invalid"))));

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
        GoogleIdToken.Payload payload = googleTokenVerifier.verify(request.getToken());
        if (payload == null) {
            throw new InvalidTokenException(getMsg("error.google.token.invalid"));
        }

        String email = payload.getEmail();
        String name = (String) payload.get("name");

        if (clientRepository.findByEmail(email).isPresent() ||
                employeeRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException(getMsg("error.user.exists"));
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
        GoogleIdToken.Payload payload = googleTokenVerifier.verify(request.getToken());
        if (payload == null) {
            throw new InvalidTokenException(getMsg("error.google.token.invalid"));
        }

        String email = payload.getEmail();

        User user = clientRepository.findByEmail(email)
                .map(u -> (User) u)
                .orElseGet(() -> employeeRepository.findByEmail(email)
                        .orElseThrow(() -> new BadCredentialsException(getMsg("error.credentials.userNotRegistered"))));

        String jwtToken = jwtUtil.generateToken(Map.of("role", user.getRole().name()), user);

        return AuthResponse.builder()
                .token(jwtToken)
                .role(user.getRole().name())
                .name(user.getName())
                .build();
    }
}