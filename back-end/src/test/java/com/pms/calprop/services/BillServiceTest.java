package com.pms.calprop.services;

import com.pms.calprop.dto.BillRequest;
import com.pms.calprop.entities.Bill;
import com.pms.calprop.exceptions.ResourceNotFoundException;
import com.pms.calprop.mappers.BillMapper;
import com.pms.calprop.repositories.BillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
public class BillServiceTest {

    @Mock
    private BillRepository billRepository;

    @Spy
    private BillMapper billMapper = Mappers.getMapper(BillMapper.class);

    @InjectMocks
    private BillService billService;

    private Bill aluguel;
    private Bill condominio;

    private final String clientId = "test-client";

    @BeforeEach
    void setUp() {
        aluguel = new Bill();
        aluguel.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        aluguel.setDescription("Aluguel");
        aluguel.setTotalAmount(new BigDecimal("1500.00"));
        aluguel.setClientId(clientId);

        condominio = new Bill();
        condominio.setId(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        condominio.setDescription("Condomínio");
        condominio.setTotalAmount(new BigDecimal("500.00"));
        condominio.setClientId(clientId);
    }

    @Test
    @DisplayName("Should create a new bill")
    void testAddBill() {
        when(billRepository.save(any(Bill.class))).thenReturn(aluguel);

        Bill savedBill = billService.addBill(new Bill());

        assertNotNull(savedBill);
        assertEquals("Aluguel", savedBill.getDescription());

        verify(billRepository, times(1)).save(any(Bill.class));
    }

    @Test
    @DisplayName("Should return all bills")
    void testFindAllBills() {
        when(billRepository.findByClientId(clientId)).thenReturn(List.of(aluguel, condominio));

        List<Bill> result = billService.findAllBills(clientId);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(billRepository, times(1)).findByClientId(clientId);
    }

    @Test
    @DisplayName("Should return a empty list when there are no bills")
    void testFindAllBillsEmpty() {
        when(billRepository.findByClientId(clientId)).thenReturn(List.of());

        List<Bill> result = billService.findAllBills(clientId);

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(billRepository, times(1)).findByClientId(clientId);
    }

    @Test
    @DisplayName("Should find a bill by ID")
    void testFindBillById() {
        when(billRepository.findByIdAndClientId(UUID.fromString("00000000-0000-0000-0000-000000000001"), clientId)).thenReturn(Optional.of(aluguel));

        Bill result = billService.findBillById(UUID.fromString("00000000-0000-0000-0000-000000000001"), clientId);

        assertNotNull(result);
        assertEquals("Aluguel", result.getDescription());

        verify(billRepository, times(1)).findByIdAndClientId(UUID.fromString("00000000-0000-0000-0000-000000000001"), clientId);
    }

    @Test
    @DisplayName("Should throw an exception when trying to find a bill that doesn't exist")
    void testNotFoundBillToFind() {
        when(billRepository.findByIdAndClientId(UUID.fromString("00000000-0000-0000-0000-000000000099"), clientId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> billService.findBillById(UUID.fromString("00000000-0000-0000-0000-000000000099"), clientId));

        assertEquals("Conta com ID 00000000-0000-0000-0000-000000000099 não encontrada!", exception.getMessage());

        verify(billRepository, times(1)).findByIdAndClientId(UUID.fromString("00000000-0000-0000-0000-000000000099"), clientId);
    }

    @Test
    @DisplayName("Should update bill data successfully")
    void testSuccessUpdateBill() {
        BillRequest updatedBill = new BillRequest("Aluguel", new BigDecimal("1450.00"));

        when(billRepository.findByIdAndClientId(UUID.fromString("00000000-0000-0000-0000-000000000001"), clientId)).thenReturn(Optional.of(aluguel));
        when(billRepository.save(any(Bill.class))).thenReturn(aluguel);

        Bill result = billService.updateBill(UUID.fromString("00000000-0000-0000-0000-000000000001"), updatedBill, clientId);

        assertNotNull(result);
        assertEquals(new BigDecimal("1450.00"), result.getTotalAmount());
        assertEquals("Aluguel", result.getDescription());
    }

    @Test
    @DisplayName("Should update a bill partially successfully, ignoring null via MapStruct")
    void testPartialUpdateBill() {
        when(billRepository.findByIdAndClientId(UUID.fromString("00000000-0000-0000-0000-000000000001"), clientId)).thenReturn(Optional.of(aluguel));

        BillRequest requestDTO = new BillRequest(null, new BigDecimal("1900.00"));

        when(billRepository.save(any(Bill.class))).thenAnswer(i -> i.getArguments()[0]);

        Bill result = billService.updateBill(UUID.fromString("00000000-0000-0000-0000-000000000001"), requestDTO, clientId);

        assertNotNull(result);
        assertEquals("Aluguel", result.getDescription());
        assertEquals(new BigDecimal("1900.00"), result.getTotalAmount());

        verify(billRepository, times(1)).findByIdAndClientId(UUID.fromString("00000000-0000-0000-0000-000000000001"), clientId);
        verify(billRepository, times(1)).save(any(Bill.class));
    }

    @Test
    @DisplayName("Should throw an expection when trying to update a bill that doesn't exist")
    void testNotFoundBillToUpdate() {
        when(billRepository.findByIdAndClientId(UUID.fromString("00000000-0000-0000-0000-000000000099"), clientId)).thenReturn(Optional.empty());

        BillRequest updatedBill = new BillRequest("Aluguel", new BigDecimal("1000.00"));

        ResourceNotFoundException exp = assertThrows(ResourceNotFoundException.class,
                () -> billService.updateBill(UUID.fromString("00000000-0000-0000-0000-000000000099"), updatedBill, clientId));

        assertEquals("Conta com ID 00000000-0000-0000-0000-000000000099 não encontrada!", exp.getMessage());

        verify(billRepository, never()).save(any(Bill.class));
    }

    @Test
    @DisplayName("Should delete a bill successfully")
    void testSuccessDeleteBill() {
        when(billRepository.findByIdAndClientId(UUID.fromString("00000000-0000-0000-0000-000000000001"), clientId)).thenReturn(Optional.of(aluguel));

        billService.deleteBill(UUID.fromString("00000000-0000-0000-0000-000000000001"), clientId);

        verify(billRepository, times(1)).findByIdAndClientId(UUID.fromString("00000000-0000-0000-0000-000000000001"), clientId);
        verify(billRepository, times(1)).deleteById(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    }

    @Test
    @DisplayName("Should throw an exception when trying to delete a bill that doesn't exist")
    void testNotFoundBillToDelete() {
        when(billRepository.findByIdAndClientId(UUID.fromString("00000000-0000-0000-0000-000000000099"), clientId)).thenReturn(Optional.empty());

        ResourceNotFoundException exp = assertThrows(ResourceNotFoundException.class,
                () -> billService.deleteBill(UUID.fromString("00000000-0000-0000-0000-000000000099"), clientId));

        assertEquals("Conta com ID 00000000-0000-0000-0000-000000000099 não encontrada!", exp.getMessage());
        verify(billRepository, never()).deleteById(any(UUID.class));
    }
}
