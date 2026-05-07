package com.pms.calprop.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pms.calprop.dto.BillRequest;
import com.pms.calprop.dto.BillResponse;
import com.pms.calprop.entities.Bill;
import com.pms.calprop.mappers.BillMapper;
import com.pms.calprop.services.BillService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/bill")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    private final BillMapper billMapper;

    @Operation(summary = "Add a new bill in calculation", description = "Need a title and total amount of de bill")
    @PostMapping
    public ResponseEntity<BillResponse> addBill(@RequestBody BillRequest request) {
        Bill bill = billMapper.toEntity(request);
        Bill savedBill = billService.addBill(bill);
        BillResponse response = billMapper.toResponse(savedBill);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all bills", description = "Return all registered bills")
    @GetMapping
    public ResponseEntity<List<BillResponse>> getAllBills() {
        List<Bill> bills = billService.findAllBills();
        List<BillResponse> responses = bills.stream()
                .map(bill -> billMapper.toResponse(bill))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get a bill by id", description = "Return a bill by id")
    @GetMapping("/{id}")
    public ResponseEntity<BillResponse> getBillById(@PathVariable Long id) {
        Bill bill = billService.findBillById(id);
        BillResponse response = billMapper.toResponse(bill);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update a bill by id", description = "Provide only the fields you want to change in the request. Null fields will be ignored and kept intact in the database.")
    @PatchMapping("/{id}")
    public ResponseEntity<BillResponse> updateBill(@PathVariable Long id, @RequestBody BillRequest request) {
        Bill updatedBill = billService.updateBill(id, request);
        BillResponse response = billMapper.toResponse(updatedBill);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a bill by id", description = "Delete a bill by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        billService.deleteBill(id);
        return ResponseEntity.noContent().build();
    }

}
