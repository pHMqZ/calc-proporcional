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

import com.pms.calprop.dto.BillDistribuition;
import com.pms.calprop.dto.PersonData;
import com.pms.calprop.entities.Bill;
import com.pms.calprop.entities.Person;
import com.pms.calprop.repositories.BillRepository;
import com.pms.calprop.repositories.PersonRepository;

@ExtendWith(MockitoExtension.class)
public class CalculationServiceTest {

    @InjectMocks
    private CalculationService calculationService;

    private Person Alceu;
    private Person Toalha;
    private Bill Aluguel;
    private Bill Internet;
    private Bill Energia;
    private Bill Condominio;

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

        Aluguel = new Bill();
        Aluguel.setId(1L);
        Aluguel.setDescription("Aluguel");
        Aluguel.setTotalAmount(new BigDecimal("1500.00"));

        Internet = new Bill();
        Internet.setId(2L);
        Internet.setDescription("Internet");
        Internet.setTotalAmount(new BigDecimal("100.00"));

        Energia = new Bill();
        Energia.setId(3L);
        Energia.setDescription("Energia");
        Energia.setTotalAmount(new BigDecimal("150.00"));

        Condominio = new Bill();
        Condominio.setId(4L);
        Condominio.setDescription("Condominio");
        Condominio.setTotalAmount(new BigDecimal("650.00"));
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

    @Test
    @DisplayName("Should calculate total bills successfully")
    void testCalculateTotalBills() {
        List<Bill> bills = List.of(Aluguel, Internet, Energia, Condominio);

        BigDecimal totalAmount = calculationService.calculateTotalBills(bills);

        assertEquals(new BigDecimal("2400.00"), totalAmount);
    }

    @Test
    @DisplayName("Should return zero when list of bills is empty")
    void testReturnZeroWhenListOfBillsIsEmpty() {
        BigDecimal totalAmount = calculationService.calculateTotalBills(List.of());

        assertEquals(BigDecimal.ZERO, totalAmount);
    }

    @Test
    @DisplayName("Should calculate bills distribuition successfully")
    void testCalculateBillsDistribuition() {
        List<Bill> bills = List.of(Aluguel, Internet, Energia, Condominio);
        List<Person> people = List.of(Alceu, Toalha);

        List<BillDistribuition> billDistribuitions = calculationService.calculateBillsDistribuition(bills, people);

        assertEquals(8, billDistribuitions.size());

        assertEquals(Aluguel.getId(), billDistribuitions.get(0).billId());
        assertEquals(Alceu.getId(), billDistribuitions.get(0).personId());
        assertEquals(Toalha.getId(), billDistribuitions.get(1).personId());
        assertEquals(new BigDecimal("681.75"), billDistribuitions.get(0).amount());
        assertEquals(new BigDecimal("818.25"), billDistribuitions.get(1).amount());

        assertEquals(Internet.getId(), billDistribuitions.get(2).billId());
        assertEquals(Alceu.getId(), billDistribuitions.get(2).personId());
        assertEquals(Toalha.getId(), billDistribuitions.get(3).personId());
        assertEquals(new BigDecimal("45.45"), billDistribuitions.get(2).amount());
        assertEquals(new BigDecimal("54.55"), billDistribuitions.get(3).amount());

        assertEquals(Energia.getId(), billDistribuitions.get(4).billId());
        assertEquals(Alceu.getId(), billDistribuitions.get(4).personId());
        assertEquals(Toalha.getId(), billDistribuitions.get(5).personId());
        assertEquals(new BigDecimal("68.18"), billDistribuitions.get(4).amount());
        assertEquals(new BigDecimal("81.82"), billDistribuitions.get(5).amount());

        assertEquals(Condominio.getId(), billDistribuitions.get(6).billId());
        assertEquals(Alceu.getId(), billDistribuitions.get(6).personId());
        assertEquals(Toalha.getId(), billDistribuitions.get(7).personId());
        assertEquals(new BigDecimal("295.43"), billDistribuitions.get(6).amount());
        assertEquals(new BigDecimal("354.57"), billDistribuitions.get(7).amount());

    }

    @Test
    @DisplayName("Should return zero when list of bill distribuitions is empty")
    void testReturnZeroWhenListOfBillDistribuitionsIsEmpty() {
        List<BillDistribuition> billDistribuitions = calculationService.calculateBillsDistribuition(List.of(),
                List.of());

        assertEquals(0, billDistribuitions.size());
    }

    @Test
    @DisplayName("Should successfully calculate the total bills for each person")
    void testCalculatePersonBillsTotal() {

        List<BillDistribuition> billsDistribuition = List.of(
                new BillDistribuition(1L, 1L, new BigDecimal("50.00"), new BigDecimal("10.00")),
                new BillDistribuition(2L, 1L, new BigDecimal("150.00"), new BigDecimal("60.00")),
                new BillDistribuition(1L, 2L, new BigDecimal("100.00"), new BigDecimal("20.00")));

        BigDecimal alceuTotal = calculationService.calculatePersonBillsTotal(Alceu, billsDistribuition);
        BigDecimal toalhaTotal = calculationService.calculatePersonBillsTotal(Toalha, billsDistribuition);

        assertEquals(new BigDecimal("200.00"), alceuTotal);
        assertEquals(new BigDecimal("100.00"), toalhaTotal);
    }

    @Test
    @DisplayName("Should successfully calculate consolidate data for a person")
    void testCalculatePersonData() {

        List<Person> people = List.of(Alceu, Toalha);
        List<Bill> bills = List.of(Aluguel, Internet, Energia, Condominio);

        BigDecimal totalSalary = calculationService.calculateTotalSalary(people);
        List<BillDistribuition> billDistribuitions = calculationService.calculateBillsDistribuition(bills, people);

        PersonData alceuData = calculationService.calculatePersonData(Alceu, totalSalary, billDistribuitions);

        assertEquals(Alceu, alceuData.person());
        assertEquals(new BigDecimal("45.45"), alceuData.percentage());
        assertEquals(new BigDecimal("250.00"), alceuData.reserveAmount());
        assertEquals(new BigDecimal("1090.81"), alceuData.billsTotal());
        assertEquals(new BigDecimal("1340.81"), alceuData.totalToPay());
        assertEquals(new BigDecimal("1159.19"), alceuData.remainingSalary());
        assertEquals(4, alceuData.billDistribuitions().size());

    }

    @Test
    @DisplayName("Should return zero when person data is called with empty list")
    void testReturnZeroWhenPersonDataIsCalledWithEmptyList() {
        List<Person> people = List.of(Alceu, Toalha);
        List<Bill> bills = List.of();

        BigDecimal totalSalary = calculationService.calculateTotalSalary(people);
        List<BillDistribuition> billDistribuitions = calculationService.calculateBillsDistribuition(bills, people);

        PersonData alceuData = calculationService.calculatePersonData(Alceu, totalSalary, billDistribuitions);

        assertEquals(0, alceuData.billDistribuitions().size());

    }

    @Test
    @DisplayName("Should successfully calculate consolidate data for all people")
    void testCalculateAllPeopleData() {
        List<Person> people = List.of(Alceu, Toalha);
        List<Bill> bills = List.of(Aluguel, Internet, Energia, Condominio);

        BigDecimal totalSalary = calculationService.calculateTotalSalary(people);
        List<BillDistribuition> billDistribuitions = calculationService.calculateBillsDistribuition(bills, people);

        List<PersonData> peopleData = calculationService.calculateAllPeopleData(people, totalSalary,
                billDistribuitions);

        assertEquals(2, peopleData.size());
        assertEquals(Alceu, peopleData.get(0).person());
        assertEquals(Toalha, peopleData.get(1).person());
    }

    @Test
    @DisplayName("Should return zero when all people data is called with empty list")
    void testReturnZeroWhenAllPeopleDataIsCalledWithEmptyList() {
        List<Person> people = List.of();
        List<Bill> bills = List.of();

        BigDecimal totalSalary = calculationService.calculateTotalSalary(people);
        List<BillDistribuition> billDistribuitions = calculationService.calculateBillsDistribuition(bills, people);

        List<PersonData> peopleData = calculationService.calculateAllPeopleData(people, totalSalary,
                billDistribuitions);

        assertEquals(0, peopleData.size());
    }
}
