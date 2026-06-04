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

public class BillValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("Should have no violation when data is valid")
    void valideBill() {
        Bill bill = new Bill(1L, "Aluguel", new BigDecimal("1500.00"), "test-client");

        Set<ConstraintViolation<Bill>> violations = validator.validate(bill);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should return violation when description is empty")
    void emptyDescription() {
        Bill bill = new Bill(1L, "", new BigDecimal("1500.00"), "test-client");

        Set<ConstraintViolation<Bill>> violations = validator.validate(bill);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should return violation when description is null")
    void nullDescription() {
        Bill bill = new Bill(1L, null, new BigDecimal("1500.00"), "test-client");

        Set<ConstraintViolation<Bill>> violations = validator.validate(bill);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should return violation when amount is null")
    void nullAmount() {
        Bill bill = new Bill(1L, "Aluguel", null, "test-client");

        Set<ConstraintViolation<Bill>> violations = validator.validate(bill);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should have no violation when amount is zero")
    void zeroAmount() {
        Bill bill = new Bill(1L, "Aluguel", BigDecimal.ZERO, "test-client");

        Set<ConstraintViolation<Bill>> violations = validator.validate(bill);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should return violation when amount is negative")
    void negativeAmount() {
        Bill bill = new Bill(1L, "Aluguel", new BigDecimal("-100.00"), "test-client");

        Set<ConstraintViolation<Bill>> violations = validator.validate(bill);

        assertFalse(violations.isEmpty());
    }
}
