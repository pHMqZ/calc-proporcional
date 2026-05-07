package com.pms.calprop.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pms.calprop.dto.BillDistribution;
import com.pms.calprop.dto.CalculationSummaryResponse;
import com.pms.calprop.dto.PersonData;
import com.pms.calprop.dto.PersonResponse;
import com.pms.calprop.entities.Bill;
import com.pms.calprop.entities.Person;
import com.pms.calprop.mappers.PersonMapper;

@ExtendWith(MockitoExtension.class)
public class CalculationServiceTest {

    @InjectMocks
    private CalculationService calculationService;

    @Mock
    private PersonMapper personMapper;

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

    private Map<Long, BigDecimal> getPercentagesMap(List<Person> people, BigDecimal totalSalary) {
        return people.stream().collect(Collectors.toMap(
                Person::getId,
                p -> calculationService.calculatePersonPercentage(p, totalSalary)));
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
        assertEquals(new BigDecimal("45.45"), alceuPercentage);
    }

    @Test
    @DisplayName("Should successfully calculate the total bills")
    void testCalculateTotalBills() {
        List<Bill> bills = List.of(Aluguel, Internet, Energia, Condominio);
        BigDecimal totalAmount = calculationService.calculateTotalBills(bills);
        assertEquals(new BigDecimal("2400.00"), totalAmount);
    }

    @Test
    @DisplayName("Should calculate bills distribution successfully")
    void testCalculateBillsDistribution() {
        List<Bill> bills = List.of(Aluguel, Internet, Energia, Condominio);
        List<Person> people = List.of(Alceu, Toalha);
        BigDecimal totalSalary = calculationService.calculateTotalSalary(people);
        Map<Long, BigDecimal> percentages = getPercentagesMap(people, totalSalary);

        List<BillDistribution> billDistributions = calculationService.calculateBillsDistribution(bills, people,
                percentages);

        assertEquals(8, billDistributions.size());
        assertThat(billDistributions.get(0).amount()).isEqualByComparingTo(new BigDecimal("681.75"));
    }

    @Test
    @DisplayName("Should successfully calculate consolidate data for a person")
    void testCalculatePersonData() {
        List<Person> people = List.of(Alceu, Toalha);
        BigDecimal totalSalary = calculationService.calculateTotalSalary(people);
        Map<Long, BigDecimal> percentages = getPercentagesMap(people, totalSalary);
        List<BillDistribution> billDistributions = calculationService.calculateBillsDistribution(List.of(Aluguel),
                people, percentages);

        when(personMapper.toResponse(any(Person.class))).thenAnswer(invocation -> {
            Person p = invocation.getArgument(0);
            return new PersonResponse(p.getId(), p.getName(), p.getSalary(), p.getReservePercentage());
        });

        PersonData alceuData = calculationService.calculatePersonData(Alceu, percentages.get(Alceu.getId()),
                billDistributions);

        assertEquals(new BigDecimal("250.00"), alceuData.reserveAmount());
        assertThat(alceuData.billsTotal()).isEqualByComparingTo(new BigDecimal("681.75"));
    }

    @Test
    @DisplayName("Should successfully calculate consolidate data for all people")
    void testCalculateAllPeopleData() {
        List<Person> people = List.of(Alceu, Toalha);
        List<Bill> bills = List.of(Aluguel, Internet, Energia, Condominio);
        BigDecimal totalSalary = calculationService.calculateTotalSalary(people);
        Map<Long, BigDecimal> percentages = getPercentagesMap(people, totalSalary);
        List<BillDistribution> billDistributions = calculationService.calculateBillsDistribution(bills, people,
                percentages);

        when(personMapper.toResponse(any(Person.class))).thenAnswer(invocation -> {
            Person p = invocation.getArgument(0);
            return new PersonResponse(p.getId(), p.getName(), p.getSalary(), p.getReservePercentage());
        });

        List<PersonData> peopleData = calculationService.calculateAllPeopleData(people, percentages, billDistributions);

        assertEquals(2, peopleData.size());
        assertThat(peopleData.get(0).billsTotal()).isEqualByComparingTo(new BigDecimal("1090.81"));
        assertThat(peopleData.get(1).billsTotal()).isEqualByComparingTo(new BigDecimal("1309.19"));
    }

    @Test
    @DisplayName("Should successfully calculate the full summary response")
    void testCalculateSummaryData() {
        List<Person> people = List.of(Alceu, Toalha);
        List<Bill> bills = List.of(Aluguel, Internet, Energia, Condominio);

        when(personMapper.toResponse(any(Person.class))).thenAnswer(invocation -> {
            Person p = invocation.getArgument(0);
            return new PersonResponse(p.getId(), p.getName(), p.getSalary(), p.getReservePercentage());
        });

        CalculationSummaryResponse summary = calculationService.calculateSummaryData(bills, people);

        assertThat(summary.totalSalary()).isEqualByComparingTo(new BigDecimal("5500"));
        assertThat(summary.totalBills()).isEqualByComparingTo(new BigDecimal("2400"));
        assertThat(summary.totalWithReserve()).isEqualByComparingTo(new BigDecimal("3250"));
        assertThat(summary.totalRemainder()).isEqualByComparingTo(new BigDecimal("2250"));
    }

    @Test
    @DisplayName("Should return empty list when bills distribution is called with empty lists")
    void testReturnZeroWhenListOfBillDistributionsIsEmpty() {
        List<Person> people = List.of(Alceu);
        BigDecimal totalSalary = calculationService.calculateTotalSalary(people);
        Map<Long, BigDecimal> percentages = getPercentagesMap(people, totalSalary);

        List<BillDistribution> billDistributions = calculationService.calculateBillsDistribution(List.of(), people,
                percentages);
        assertEquals(0, billDistributions.size());
    }

    @Test
    @DisplayName("Should successfully calculate the total bills for each person")
    void testCalculatePersonBillsTotal() {
        List<BillDistribution> billDistributions = List.of(
                new BillDistribution(1L, "Aluguel", Alceu.getId(), new BigDecimal("50.00"), new BigDecimal("10.00")),
                new BillDistribution(2L, "Internet", Alceu.getId(), new BigDecimal("150.00"), new BigDecimal("60.00")),
                new BillDistribution(1L, "Aluguel", Toalha.getId(), new BigDecimal("100.00"), new BigDecimal("20.00")));
        BigDecimal alceuTotal = calculationService.calculatePersonBillsTotal(Alceu, billDistributions);
        BigDecimal toalhaTotal = calculationService.calculatePersonBillsTotal(Toalha, billDistributions);

        assertThat(alceuTotal).isEqualByComparingTo(new BigDecimal("200.00"));
        assertThat(toalhaTotal).isEqualByComparingTo(new BigDecimal("100.00"));
    }
}
