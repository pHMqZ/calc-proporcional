package com.pms.calprop.services;

import com.pms.calprop.dto.BillRequest;
import com.pms.calprop.entities.Bill;
import com.pms.calprop.exceptions.ResourceNotFoundException;
import com.pms.calprop.mappers.BillMapper;
import com.pms.calprop.repositories.BillRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

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

    public List<Bill> findAllBills() {
        return billRepository.findAll();
    }

    public Bill findBillById(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conta com ID " + id + " não encontrada!"));
    }

    public Bill updateBill(Long id, BillRequest request) {
        Bill existingBill = this.findBillById(id);

        billMapper.updateBillFromRequest(request, existingBill);

        return billRepository.save(existingBill);
    }

    public void deleteBill(Long id) {
        this.findBillById(id);
        billRepository.deleteById(id);
    }

}
