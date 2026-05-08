package com.pms.calprop.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.pms.calprop.dto.CalculationSummaryResponse;
import com.pms.calprop.entities.Bill;
import com.pms.calprop.entities.Person;
import com.pms.calprop.services.BillService;
import com.pms.calprop.services.CalculationService;
import com.pms.calprop.services.PersonService;

@WebMvcTest(CalculationController.class)
public class CalculationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService personService;

    @MockitoBean
    private BillService billService;

    @MockitoBean
    private CalculationService calculationService;

    @Test
    @DisplayName("Should return the full calculation summary from database")
    void testCalculateDistribution() throws Exception {
        List<Person> people = new ArrayList<>();
        people.add(new Person("Alceu", new BigDecimal("2500"), 10.0));
        people.add(new Person("Toalha", new BigDecimal("3000"), 20.0));

        List<Bill> bills = new ArrayList<>();
        bills.add(new Bill("Aluguel", new BigDecimal("1500")));
        bills.add(new Bill("Condominio", new BigDecimal("650")));
        bills.add(new Bill("Internet", new BigDecimal("100")));
        bills.add(new Bill("Energia", new BigDecimal("150")));

        CalculationSummaryResponse expectedResponse = new CalculationSummaryResponse(
                new BigDecimal("5500"),
                new BigDecimal("2400"),
                new BigDecimal("3250"),
                new BigDecimal("2250"),
                List.of(),
                List.of());

        when(personService.findAllPeople()).thenReturn(people);
        when(billService.findAllBills()).thenReturn(bills);
        when(calculationService.calculateSummaryData(bills, people)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/v1/calculation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSalary").value(expectedResponse.totalSalary().doubleValue()))
                .andExpect(jsonPath("$.totalBills").value(expectedResponse.totalBills().doubleValue()))
                .andExpect(jsonPath("$.totalWithReserve").value(expectedResponse.totalWithReserve().doubleValue()))
                .andExpect(jsonPath("$.totalRemainder").value(expectedResponse.totalRemainder().doubleValue()));
    }

}
