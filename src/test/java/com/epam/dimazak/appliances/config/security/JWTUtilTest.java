package com.epam.dimazak.appliances.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JWTUtilTest {

    @InjectMocks
    private JWTUtil jwtUtil;

    private String secretKey = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970"; // Mock secret key
    private long expiration = 1000 * 60 * 60; // 1 hour

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtil, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", expiration);
    }

    @Test
    void extractUsername_shouldReturnUsernameFromToken() {
        UserDetails userDetails = new User("test@example.com", "password", Collections.emptyList());
        String token = jwtUtil.generateToken(userDetails);

        String username = jwtUtil.extractUsername(token);

        assertThat(username).isEqualTo("test@example.com");
    }

    @Test
    void generateToken_shouldCreateValidToken() {
        UserDetails userDetails = new User("test@example.com", "password", Collections.emptyList());
        String token = jwtUtil.generateToken(userDetails);

        assertThat(token).isNotNull();
        assertThat(jwtUtil.isTokenValid(token, userDetails)).isTrue();
    }

    @Test
    void generateToken_withExtraClaims_shouldIncludeClaims() {
        UserDetails userDetails = new User("test@example.com", "password", Collections.emptyList());
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");

        String token = jwtUtil.generateToken(extraClaims, userDetails);

        assertThat(token).isNotNull();
        // Manually decode to check claim? Or use extractClaim
        String role = jwtUtil.extractClaim(token, claims -> claims.get("role", String.class));
        assertThat(role).isEqualTo("ADMIN");
        assertThat(jwtUtil.extractUsername(token)).isEqualTo("test@example.com");
    }

    @Test
    void isTokenValid_whenTokenIsValid_shouldReturnTrue() {
        UserDetails userDetails = new User("test@example.com", "password", Collections.emptyList());
        String token = jwtUtil.generateToken(userDetails);

        boolean isValid = jwtUtil.isTokenValid(token, userDetails);

        assertThat(isValid).isTrue();
    }

    @Test
    void isTokenValid_whenUsernameIsDifferent_shouldReturnFalse() {
        UserDetails userDetails = new User("test@example.com", "password", Collections.emptyList());
        String token = jwtUtil.generateToken(userDetails);

        UserDetails otherUser = new User("other@example.com", "password", Collections.emptyList());

        boolean isValid = jwtUtil.isTokenValid(token, otherUser);

        assertThat(isValid).isFalse();
    }

    @Test
    void isTokenExpired_shouldReturnTrue_ForExpiredToken() throws InterruptedException {
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", 1L); // 1ms
        UserDetails userDetails = new User("test@example.com", "password", Collections.emptyList());
        String token = jwtUtil.generateToken(userDetails);

        Thread.sleep(10);

        try {
            jwtUtil.isTokenValid(token, userDetails);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(io.jsonwebtoken.ExpiredJwtException.class);
        }
    }
}
