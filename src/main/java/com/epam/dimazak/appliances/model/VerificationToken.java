package com.epam.dimazak.appliances.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "verification_token")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Enumerated(EnumType.STRING)
    private TokenType type;

    public enum TokenType {
        EMAIL_VERIFICATION,
        PASSWORD_RESET
    }

    public VerificationToken(String email, TokenType type, int durationInMinutes) {
        this.token = UUID.randomUUID().toString();
        this.email = email;
        this.type = type;
        this.expiryDate = LocalDateTime.now().plusMinutes(durationInMinutes);
    }
}