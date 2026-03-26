package com.pms.calprop.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pms.calprop.dto.BillRequest;
import com.pms.calprop.dto.BillResponse;
import com.pms.calprop.entities.Bill;
import com.pms.calprop.exceptions.ResourceNotFoundException;
import com.pms.calprop.mappers.BillMapper;
import com.pms.calprop.services.BillService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/bill")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    private final BillMapper billMapper;

    @PostMapping
    public ResponseEntity<BillResponse> addBill(@RequestBody BillRequest request) {
        Bill bill = billMapper.toEntity(request);
        Bill savedBill = billService.addBill(bill);
        BillResponse response = billMapper.toResponse(savedBill);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BillResponse>> getAllBills() {
        List<Bill> bills = billService.findAllBills();
        List<BillResponse> responses = bills.stream()
                .map(bill -> billMapper.toResponse(bill))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillResponse> getBillById(@PathVariable Long id) {
        Bill bill = billService.findBillById(id);
        BillResponse response = billMapper.toResponse(bill);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFound(ResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", exception.getMessage()));
    }
}
