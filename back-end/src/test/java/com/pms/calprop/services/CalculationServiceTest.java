package com.pms.calprop.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pms.calprop.entities.Person;
import com.pms.calprop.repositories.BillRepository;
import com.pms.calprop.repositories.PersonRepository;

@ExtendWith(MockitoExtension.class)
public class CalculationServiceTest {

    @InjectMocks
    private CalculationService calculationService;

    private Person Alceu;
    private Person Toalha;

    @BeforeEach
    void setUp() {
        Alceu = new Person();
        Alceu.setId(1L);
        Alceu.setName("Alceu");
        Alceu.setSalary(new BigDecimal("2500.00"));
        Alceu.setReservePercentage(10.0);

        Toalha = new Person();
        Toalha.setId(2L);
        Toalha.setName("Toalha");
        Toalha.setSalary(new BigDecimal("3000.00"));
        Toalha.setReservePercentage(20.0);
    }

    @Test
    @DisplayName("Should calculate total salary successfully")
    void testCalculateTotalSalary() {
        List<Person> people = List.of(Alceu, Toalha);

        BigDecimal totalSalary = calculationService.calculateTotalSalary(people);

        assertEquals(new BigDecimal("5500.00"), totalSalary);
    }

    @Test
    @DisplayName("Should calculate person percentage successfully")
    void testCalculatePersonPercentage() {
        BigDecimal totalSalary = calculationService.calculateTotalSalary(List.of(Alceu, Toalha));

        BigDecimal alceuPercentage = calculationService.calculatePersonPercentage(Alceu, totalSalary);
        BigDecimal toalhaPercentage = calculationService.calculatePersonPercentage(Toalha, totalSalary);

        assertEquals(new BigDecimal("45.45"), alceuPercentage);
        assertEquals(new BigDecimal("54.55"), toalhaPercentage);
    }

    @Test
    @DisplayName("Should return zero when list of person is empty")
    void testReturnZeroWhenListOfPersonIsEmpty() {
        BigDecimal totalSalary = calculationService.calculateTotalSalary(List.of());

        assertEquals(BigDecimal.ZERO, totalSalary);
    }

    @Test
    @DisplayName("Should return zero  when total salary is zero")
    void testCalculatePercentageWhenTotalSalaryIsZero() {
        BigDecimal totalSalary = BigDecimal.ZERO;

        BigDecimal alceuPercentage = calculationService.calculatePersonPercentage(Alceu, totalSalary);
        BigDecimal toalhaPercentage = calculationService.calculatePersonPercentage(Toalha, totalSalary);

        assertEquals(BigDecimal.ZERO, alceuPercentage);
        assertEquals(BigDecimal.ZERO, toalhaPercentage);
    }

    @Test
    @DisplayName("Should calculate reserve amount for person successfully")
    void testCalculateReserveAmountForPerson() {

        BigDecimal alceuReserveAmount = calculationService.calculateReserveAmount(Alceu);
        BigDecimal toalhaReserveAmount = calculationService.calculateReserveAmount(Toalha);

        assertEquals(new BigDecimal("250.00"), alceuReserveAmount);
        assertEquals(new BigDecimal("600.00"), toalhaReserveAmount);
    }

    @Test
    @DisplayName("Should calculate percentage when total reserve amount is zero")
    void testCalculatePercentageWhenTotalReserveAmountIsZero() {
        Alceu.setReservePercentage(0.0);
        Toalha.setReservePercentage(0.0);

        BigDecimal alceuReserveAmount = calculationService.calculateReserveAmount(Alceu);
        BigDecimal toalhaReserveAmount = calculationService.calculateReserveAmount(Toalha);

        assertEquals(new BigDecimal("0.00"), alceuReserveAmount);
        assertEquals(new BigDecimal("0.00"), toalhaReserveAmount);
    }

}
