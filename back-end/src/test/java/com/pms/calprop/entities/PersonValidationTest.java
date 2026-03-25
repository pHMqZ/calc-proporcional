package com.pms.calprop.entities;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PersonValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("Should have no violation when data is valid")
    void validPerson() {
        Person person = new Person(1L, "João", new BigDecimal("1500.00"), 10.0);

        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should return violation when name is empty")
    void emptyName() {
        Person person = new Person(1L, "", new BigDecimal("1500.00"), 10.0);

        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should return violation when name is null")
    void nullName() {
        Person person = new Person(1L, null, new BigDecimal("1500.00"), 10.0);

        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should return violation when salary is null")
    void nullSalary() {
        Person person = new Person(1L, "João", null, 10.0);

        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should return violation when salary is zero")
    void zeroSalary() {
        Person person = new Person(1L, "João", BigDecimal.ZERO, 10.0);

        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should return violation when salary is negative")
    void negativeSalary() {
        Person person = new Person(1L, "João", new BigDecimal("-100.00"), 10.0);

        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should return violation when reservePercentage is null")
    void nullReservePercentage() {
        Person person = new Person(1L, "João", new BigDecimal("1500.00"), null);

        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should return violation when reservePercentage is negative")
    void negativeReservePercentage() {
        Person person = new Person(1L, "João", new BigDecimal("1500.00"), -10.0);

        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should return violation when reservePercentage is greater than 100")
    void greaterThan100ReservePercentage() {
        Person person = new Person(1L, "João", new BigDecimal("1500.00"), 110.0);

        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        assertFalse(violations.isEmpty());
    }
}
