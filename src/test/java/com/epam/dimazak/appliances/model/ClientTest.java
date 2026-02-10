package com.epam.dimazak.appliances.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ClientTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsValid_thenValidationSucceeds() {
        Client client = new Client(1L, "John Doe", "john@example.com", "password123", "1234567890123456", true);
        Set<ConstraintViolation<Client>> violations = validator.validate(client);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenEmailInvalid_thenValidationFails() {
        Client client = new Client(1L, "John Doe", "invalid-email", "password123", "1234567890123456", true);
        Set<ConstraintViolation<Client>> violations = validator.validate(client);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void whenPasswordShort_thenValidationFails() {
        Client client = new Client(1L, "John Doe", "john@example.com", "123", "1234567890123456", true);
        Set<ConstraintViolation<Client>> violations = validator.validate(client);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    void whenCardLengthInvalid_thenValidationFails() {
        Client client = new Client(1L, "John Doe", "john@example.com", "password123", "123", true);
        Set<ConstraintViolation<Client>> violations = validator.validate(client);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("card"));
    }
}
