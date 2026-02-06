package com.epam.dimazak.appliances.repository;

import com.epam.dimazak.appliances.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    void deleteByEmailAndType(String email, VerificationToken.TokenType type);
}