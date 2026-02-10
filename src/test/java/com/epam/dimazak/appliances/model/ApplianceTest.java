package com.epam.dimazak.appliances.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ApplianceTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsValid_thenValidationSucceeds() {
        Appliance appliance = new Appliance();
        appliance.setNameEn("Fridge");
        appliance.setNameUa("Холодильник");
        appliance.setPrice(BigDecimal.valueOf(100.00));
        appliance.setPower(200);

        Set<ConstraintViolation<Appliance>> violations = validator.validate(appliance);

        assertThat(violations).isEmpty();
    }

    @Test
    void whenNameEnIsNull_thenValidationFails() {
        Appliance appliance = new Appliance();
        appliance.setNameEn(null);
        appliance.setNameUa("Холодильник");
        appliance.setPrice(BigDecimal.valueOf(100.00));
        appliance.setPower(200);

        Set<ConstraintViolation<Appliance>> violations = validator.validate(appliance);

        assertThat(violations).hasSize(1);
    }

    @Test
    void whenNameUaIsNull_thenValidationFails() {
        Appliance appliance = new Appliance();
        appliance.setNameEn("Fridge");
        appliance.setNameUa(null);
        appliance.setPrice(BigDecimal.valueOf(100.00));
        appliance.setPower(200);

        Set<ConstraintViolation<Appliance>> violations = validator.validate(appliance);

        assertThat(violations).hasSize(1);
    }

    @Test
    void whenPriceIsNull_thenValidationFails() {
        Appliance appliance = new Appliance();
        appliance.setNameEn("Fridge");
        appliance.setNameUa("Холодильник");
        appliance.setPrice(null);
        appliance.setPower(200);

        Set<ConstraintViolation<Appliance>> violations = validator.validate(appliance);

        assertThat(violations).isNotEmpty();
    }

    @Test
    void whenPriceIsNegative_thenValidationFails() {
        Appliance appliance = new Appliance();
        appliance.setNameEn("Fridge");
        appliance.setNameUa("Холодильник");
        appliance.setPrice(BigDecimal.valueOf(-10.00));
        appliance.setPower(200);

        Set<ConstraintViolation<Appliance>> violations = validator.validate(appliance);

        assertThat(violations).isNotEmpty();
    }

    @Test
    void whenPowerIsNegative_thenValidationFails() {
        Appliance appliance = new Appliance();
        appliance.setNameEn("Fridge");
        appliance.setNameUa("Холодильник");
        appliance.setPrice(BigDecimal.valueOf(100.00));
        appliance.setPower(-50);

        Set<ConstraintViolation<Appliance>> violations = validator.validate(appliance);

        assertThat(violations).isNotEmpty();
    }
}
