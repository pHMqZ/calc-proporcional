package com.pms.calprop.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pms.calprop.dto.BillRequest;
import com.pms.calprop.dto.BillResponse;
import com.pms.calprop.entities.Bill;
import com.pms.calprop.exceptions.ResourceNotFoundException;
import com.pms.calprop.mappers.BillMapper;
import com.pms.calprop.services.BillService;

@WebMvcTest(BillController.class)
@SuppressWarnings("null")
public class BillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BillService billService;

    @MockitoBean
    private BillMapper billMapper;

    private final String clientId = "test-client-id";

    @Test
    @DisplayName("Should create a new bill successfully")
    void testCreateBill() throws Exception {
        BillRequest requestDTO = new BillRequest("Aluguel", new BigDecimal("1500.00"));
        Bill bill = new Bill();
        bill.setId(1L);
        bill.setDescription("Aluguel");
        bill.setTotalAmount(new BigDecimal("1500.00"));
        bill.setClientId(clientId);
        BillResponse response = new BillResponse(1L, "Aluguel", new BigDecimal("1500.00"));

        when(billMapper.toEntity(any(BillRequest.class))).thenReturn(bill);
        when(billService.addBill(any(Bill.class))).thenReturn(bill);
        when(billMapper.toResponse(any(Bill.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/bill")
                .header("X-Client-Id", clientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Aluguel"));
    }

    @Test
    @DisplayName("Should get all bills successfully")
    void testGetAllBills() throws Exception {
        when(billService.findAllBills(clientId)).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/api/v1/bill")
                .header("X-Client-Id", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    @DisplayName("Should get a bill by id successfully")
    void testGetBillById() throws Exception {
        Bill bill = new Bill();
        BillResponse response = new BillResponse(1L, "Aluguel", new BigDecimal("1500.00"));

        when(billService.findBillById(1L, clientId)).thenReturn(bill);
        when(billMapper.toResponse(any(Bill.class))).thenReturn(response);

        mockMvc.perform(get("/api/v1/bill/1")
                .header("X-Client-Id", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Aluguel"));
    }

    @Test
    @DisplayName("Should return a ResourceNotFoundException when the bill is not found")
    void testGetBillByIdNotFound() throws Exception {
        when(billService.findBillById(99L, clientId))
                .thenThrow(new ResourceNotFoundException("Conta com ID 99 não encontrada!"));

        mockMvc.perform(get("/api/v1/bill/99")
                .header("X-Client-Id", clientId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Conta com ID 99 não encontrada!"));
    }

    @Test
    @DisplayName("Should update a bill successfully")
    void testSuccessUpdatedBill() throws Exception {
        BillRequest request = new BillRequest("Aluguel", new BigDecimal("1500.00"));
        Bill bill = new Bill();
        BillResponse response = new BillResponse(1L, "Aluguel", new BigDecimal("1500.00"));

        when(billService.updateBill(eq(1L), any(BillRequest.class), eq(clientId))).thenReturn(bill);
        when(billMapper.toResponse(any(Bill.class))).thenReturn(response);

        mockMvc.perform(patch("/api/v1/bill/1")
                .header("X-Client-Id", clientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("Should delete a bill successfully")
    void testDeleteABillSuccessfully() throws Exception {
        mockMvc.perform(delete("/api/v1/bill/1")
                .header("X-Client-Id", clientId))
                .andExpect(status().isNoContent());
    }
}
