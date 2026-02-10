package com.epam.dimazak.appliances.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserHierarchyTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testClientCreationAndValidation() {
        Client client = new Client(1L, "John Doe", "john@example.com", "password123", "1234123412341234", true);

        assertThat(client.getId()).isEqualTo(1L);
        assertThat(client.getName()).isEqualTo("John Doe");
        assertThat(client.getRole()).isEqualTo(Role.CLIENT); // Перевіряємо, що конструктор Client ставить правильну роль
        assertThat(client.getCard()).isEqualTo("1234123412341234");
        assertThat(client.isEnabled()).isTrue();

        // Перевірка getAuthorities з класу User
        assertThat(client.getAuthorities()).hasSize(1);
        assertThat(client.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_CLIENT");

        // Валідація (успішна)
        Set<ConstraintViolation<Client>> violations = validator.validate(client);
        assertThat(violations).isEmpty();
    }

    @Test
    void testClientValidationFailure() {
        // Картка неправильної довжини, невалідний email
        Client client = new Client(null, "", "invalid-email", "short", "123", true);

        Set<ConstraintViolation<Client>> violations = validator.validate(client);

        assertThat(violations).hasSizeGreaterThan(0);
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Email should be valid"));
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name")); // @NotBlank
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("card")); // @Size
    }

    @Test
    void testEmployeeCreation() {
        Employee employee = new Employee(2L, "Jane Staff", "jane@corp.com", "securePass", "IT", true);

        assertThat(employee.getRole()).isEqualTo(Role.EMPLOYEE);
        assertThat(employee.getDepartment()).isEqualTo("IT");
        assertThat(employee.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_EMPLOYEE");
    }
}