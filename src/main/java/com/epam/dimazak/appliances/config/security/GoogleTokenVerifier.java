package com.epam.dimazak.appliances.config.security;

import com.epam.dimazak.appliances.exception.InvalidTokenException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@Slf4j
public class GoogleTokenVerifier {

    @Value("${application.security.google.client-id}")
    private String clientId;

    public GoogleIdToken.Payload verify(String tokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    new GsonFactory()
            )
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(tokenString);

            if (idToken != null) {
                return idToken.getPayload();
            } else {
                throw new InvalidTokenException("Invalid Google ID token");
            }
        } catch (Exception e) {
            log.error("Google token verification failed", e);
            throw new InvalidTokenException("Google authentication failed");
        }
    }
}