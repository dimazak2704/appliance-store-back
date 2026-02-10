package com.epam.dimazak.appliances.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsValid_thenValidationSucceeds() {
        Category category = new Category();
        category.setNameEn("Electronics");
        category.setNameUa("Електроніка");

        Set<ConstraintViolation<Category>> violations = validator.validate(category);

        assertThat(violations).isEmpty();
    }

    @Test
    void whenNameEnIsBlank_thenValidationFails() {
        Category category = new Category();
        category.setNameEn("");
        category.setNameUa("Електроніка");

        Set<ConstraintViolation<Category>> violations = validator.validate(category);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("nameEn"));
    }

    @Test
    void whenNameUaIsNull_thenValidationFails() {
        Category category = new Category();
        category.setNameEn("Electronics");
        category.setNameUa(null);

        Set<ConstraintViolation<Category>> violations = validator.validate(category);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("nameUa"));
    }
}
