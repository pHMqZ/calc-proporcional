package com.pms.calprop.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pms.calprop.dto.BillRequest;
import com.pms.calprop.entities.Bill;
import com.pms.calprop.exceptions.ResourceNotFoundException;
import com.pms.calprop.mappers.BillMapperImpl;
import com.pms.calprop.services.BillService;

@WebMvcTest(BillController.class)
@Import(BillMapperImpl.class)
@SuppressWarnings("null")
public class BillControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private BillService billService;

        @Test
        @DisplayName("Should create a new bill successfully")
        void testCreateBill() throws Exception {
                BillRequest requestDTO = new BillRequest("Aluguel", new BigDecimal("1500.00"));

                Bill savedBillMock = new Bill();
                savedBillMock.setId(1L);
                savedBillMock.setDescription(requestDTO.description());
                savedBillMock.setTotalAmount(requestDTO.totalAmount());

                when(billService.addBill(any(Bill.class))).thenReturn(savedBillMock);

                mockMvc.perform(post("/api/v1/bill").contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1L))
                                .andExpect(jsonPath("$.description").value(requestDTO.description()))
                                .andExpect(jsonPath("$.totalAmount").value(requestDTO.totalAmount().doubleValue()));
        }

        @Test
        @DisplayName("Should get all bills successfully")
        void testGetAllBills() throws Exception {

                BillRequest aluguelDTO = new BillRequest("Aluguel", new BigDecimal("1500.00"));

                BillRequest condominioDTO = new BillRequest("Condominio", new BigDecimal("500.00"));

                Bill aluguel = new Bill();
                aluguel.setId(1L);
                aluguel.setDescription(aluguelDTO.description());
                aluguel.setTotalAmount(aluguelDTO.totalAmount());

                Bill condominio = new Bill();
                condominio.setId(2L);
                condominio.setDescription(condominioDTO.description());
                condominio.setTotalAmount(condominioDTO.totalAmount());

                List<Bill> bills = Arrays.asList(aluguel, condominio);

                when(billService.findAllBills()).thenReturn(bills);

                mockMvc.perform(get("/api/v1/bill"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.size()").value(2))
                                .andExpect(jsonPath("$.[0].id").value(1L))
                                .andExpect(jsonPath("$.[0].description").value(aluguelDTO.description()))
                                .andExpect(jsonPath("$.[0].totalAmount").value(aluguelDTO.totalAmount().doubleValue()))
                                .andExpect(jsonPath("$.[1].id").value(2L))
                                .andExpect(jsonPath("$.[1].description").value(condominioDTO.description()))
                                .andExpect(jsonPath("$.[1].totalAmount")
                                                .value(condominioDTO.totalAmount().doubleValue()));
        }

        @Test
        @DisplayName("Should return a empty list when there are no bills")
        void testGetAllBillsEmpty() throws Exception {
                when(billService.findAllBills()).thenReturn(new ArrayList<>());
                mockMvc.perform(get("/api/v1/bill"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.size()").value(0));
        }

        @Test
        @DisplayName("Should get a bill by id successfully")
        void testGetBillById() throws Exception {

                BillRequest aluguelDTO = new BillRequest("Aluguel", new BigDecimal("1500.00"));

                Bill aluguel = new Bill();
                aluguel.setId(1L);
                aluguel.setDescription(aluguelDTO.description());
                aluguel.setTotalAmount(aluguelDTO.totalAmount());

                when(billService.findBillById(1L)).thenReturn(aluguel);

                mockMvc.perform(get("/api/v1/bill/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1L))
                                .andExpect(jsonPath("$.description").value(aluguelDTO.description()))
                                .andExpect(jsonPath("$.totalAmount").value(aluguelDTO.totalAmount().doubleValue()));
        }

        @Test
        @DisplayName("Should return a ResourceNotFoundException when the bill is not found")
        void testGetBillByIdNotFound() throws Exception {

                when(billService.findBillById(99L))
                                .thenThrow(new ResourceNotFoundException("Conta com ID 99 não encontrada!"));

                mockMvc.perform(get("/api/v1/bill/99"))
                                .andExpect(jsonPath("$.timestamp").exists())
                                .andExpect(jsonPath("$.status").value(404))
                                .andExpect(jsonPath("$.error").value("Resource Not Found"))
                                .andExpect(jsonPath("$.message").value("Conta com ID 99 não encontrada!"))
                                .andExpect(jsonPath("$.path").value("/api/v1/bill/99"));
        }

        @Test
        @DisplayName("Should update a bill successfully")
        void testSuccessUpdatedBill() throws Exception {

                BillRequest updatedBillDTO = new BillRequest("Aluguel", new BigDecimal("1500.00"));

                Bill updatedBill = new Bill();
                updatedBill.setId(1L);
                updatedBill.setDescription(updatedBillDTO.description());
                updatedBill.setTotalAmount(updatedBillDTO.totalAmount());

                when(billService.updateBill(eq(1L), any(BillRequest.class))).thenReturn(updatedBill);

                mockMvc.perform(patch("/api/v1/bill/1").contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedBillDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1L))
                                .andExpect(jsonPath("$.description").value(updatedBillDTO.description()))
                                .andExpect(jsonPath("$.totalAmount").value(updatedBillDTO.totalAmount().doubleValue()));
        }

        @Test
        @DisplayName("Should return a ResourceNotFoundException when the bill is not found")
        void testThrowExceptionWhenUpdatingBillNotFound() throws Exception {

                BillRequest updatedBillDTO = new BillRequest("Aluguel", new BigDecimal("1500.00"));

                when(billService.updateBill(eq(99L), any(BillRequest.class)))
                                .thenThrow(new ResourceNotFoundException("Conta com ID 99 não encontrada!"));

                mockMvc.perform(patch("/api/v1/bill/99").contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedBillDTO)))
                                .andExpect(jsonPath("$.timestamp").exists())
                                .andExpect(jsonPath("$.status").value(404))
                                .andExpect(jsonPath("$.error").value("Resource Not Found"))
                                .andExpect(jsonPath("$.message").value("Conta com ID 99 não encontrada!"))
                                .andExpect(jsonPath("$.path").value("/api/v1/bill/99"));
        }

        @Test
        @DisplayName("Should delete a bill successfully")
        void testDeleteABillSuccessfully() throws Exception {

                mockMvc.perform(delete("/api/v1/bill/1"))
                                .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Should return a ResourceNotFoundException when the bill is not found")
        void testThrowExceptionWhenDeletingBillNotFound() throws Exception {

                Mockito.doThrow(new ResourceNotFoundException("Conta com ID 99 não encontrada!")).when(billService)
                                .deleteBill(99L);

                mockMvc.perform(delete("/api/v1/bill/99"))
                                .andExpect(jsonPath("$.timestamp").exists())
                                .andExpect(jsonPath("$.status").value(404))
                                .andExpect(jsonPath("$.error").value("Resource Not Found"))
                                .andExpect(jsonPath("$.message").value("Conta com ID 99 não encontrada!"))
                                .andExpect(jsonPath("$.path").value("/api/v1/bill/99"));
        }

}
