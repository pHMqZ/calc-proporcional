package com.pms.calprop.services;

import com.pms.calprop.dto.BillRequest;
import com.pms.calprop.entities.Bill;
import com.pms.calprop.exceptions.ResourceNotFoundException;
import com.pms.calprop.mappers.BillMapper;
import com.pms.calprop.repositories.BillRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class BillService {

    private final BillRepository billRepository;

    private final BillMapper billMapper;

    public Bill addBill(Bill newBill) {
        return billRepository.save(newBill);
    }

    public List<Bill> findAllBills(String clientId) {
        return billRepository.findByClientId(clientId);
    }

    public Bill findBillById(UUID id, String clientId) {
        return billRepository.findByIdAndClientId(id, clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Conta com ID " + id + " não encontrada!"));
    }

    public Bill updateBill(UUID id, BillRequest request, String clientId) {
        Bill existingBill = this.findBillById(id, clientId);

        billMapper.updateBillFromRequest(request, existingBill);

        return billRepository.save(existingBill);
    }

    public void deleteBill(UUID id, String clientId) {
        this.findBillById(id, clientId);
        billRepository.deleteById(id);
    }

}
