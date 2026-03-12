package com.pms.calprop.services;

import com.pms.calprop.entities.Bill;
import com.pms.calprop.repositories.BillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BillService {

    private BillRepository billRepository;

    public Bill addBill(Bill newBill) {
        return billRepository.save(newBill);
    }

    public Bill updateBill(Long id, Bill updatedBill) {
        Bill existingBill = billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta com ID "+ id + " não encontrada!"));

        existingBill.setDescription(updatedBill.getDescription());
        existingBill.setTotalAmount(updatedBill.getTotalAmount());

        return billRepository.save(existingBill);
    }

    public void deleteBill(Long id) {
        if (!billRepository.existsById(id)){
            throw new RuntimeException("Conta com ID " + id + " não encontrada!");
        }
        billRepository.deleteById(id);
    }
}
