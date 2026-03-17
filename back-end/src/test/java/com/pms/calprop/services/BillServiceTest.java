package com.pms.calprop.services;

import com.pms.calprop.entities.Bill;
import com.pms.calprop.repositories.BillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
public class BillServiceTest {

    @Mock
    private BillRepository billRepository;

    @InjectMocks
    private BillService billService;

    private Bill bill;

    @BeforeEach
    void setUp() {
        bill = new Bill();
        bill.setId(1L);
        bill.setDescription("Aluguel");
        bill.setTotalAmount(new BigDecimal("1500.00"));
    }

    @Test
    @DisplayName("Should create a new bill")
    void testAddBill () {
        when(billRepository.save(any(Bill.class))).thenReturn(bill);

        Bill savedBill = billService.addBill(new Bill());

        assertNotNull(savedBill);
        assertEquals("Aluguel", savedBill.getDescription());

        verify(billRepository, times(1)).save(any(Bill.class));
    }

    @Test
    @DisplayName("Should update bill data successfully")
    void testSuccessUpdateBill(){
        Bill updatedBill = new Bill();
        updatedBill.setTotalAmount(new BigDecimal("1430.00"));
        updatedBill.setDescription("Aluguel + IPTU");

        when(billRepository.findById(1L)).thenReturn(Optional.of(bill));
        when(billRepository.save(any(Bill.class))).thenReturn(bill);

        Bill result = billService.updateBill(1L, updatedBill);

        assertNotNull(result);
        assertEquals(new BigDecimal("1430.00"), result.getTotalAmount());
        assertEquals("Aluguel + IPTU", result.getDescription());
    }

    @Test
    @DisplayName("Should throw an expection when trying to update a bill that doesn't exist")
    void testNotFoundBillToUpdate() {
        when(billRepository.findById(99L)).thenReturn(Optional.empty());

        Bill updatedBill = new Bill();
        updatedBill.setTotalAmount(new BigDecimal("1000.00"));

        RuntimeException exp = assertThrows(RuntimeException.class, ( ) ->
                billService.updateBill(99L, updatedBill));

        assertEquals("Conta com ID 99 não encontrada!", exp.getMessage());

        verify(billRepository,never()).save(any(Bill.class));
    }

    @Test
    @DisplayName("Should delete a bill successfully")
    void testSuccessDeleteBill() {
        when(billRepository.existsById(1L)).thenReturn(true);

        billService.deleteBill(1L);

        verify(billRepository, times(1)).existsById(1L);
        verify(billRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw an exception when trying to delete a bill that doesn't exist")
    void testNotFoundBillToDelete() {
        when(billRepository.existsById(99L)).thenReturn(false);

        RuntimeException exp = assertThrows(RuntimeException.class, ( ) ->
                billService.deleteBill(99L));

        assertEquals("Conta com ID 99 não encontrada!", exp.getMessage());
        verify(billRepository, never()).deleteById(anyLong());
    }
}
