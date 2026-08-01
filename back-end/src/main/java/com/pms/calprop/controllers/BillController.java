package com.pms.calprop.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public ResponseEntity<BillResponse> addBill(@RequestHeader(value = "X-Client-Id", required = true) String clientId,
            @RequestBody BillRequest request) {
        Bill bill = billMapper.toEntity(request);
        bill.setClientId(clientId);
        Bill savedBill = billService.addBill(bill);
        BillResponse response = billMapper.toResponse(savedBill);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all bills", description = "Return all registered bills")
    @GetMapping
    public ResponseEntity<List<BillResponse>> getAllBills(
            @RequestHeader(value = "X-Client-Id", required = true) String clientId) {
        List<Bill> bills = billService.findAllBills(clientId);
        List<BillResponse> responses = bills.stream()
                .map(bill -> billMapper.toResponse(bill))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get a bill by id", description = "Return a bill by id")
    @GetMapping("/{id}")
    public ResponseEntity<BillResponse> getBillById(
            @RequestHeader(value = "X-Client-Id", required = true) String clientId, @PathVariable UUID id) {
        Bill bill = billService.findBillById(id, clientId);
        BillResponse response = billMapper.toResponse(bill);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update a bill by id", description = "Provide only the fields you want to change in the request. Null fields will be ignored and kept intact in the database.")
    @PatchMapping("/{id}")
    public ResponseEntity<BillResponse> updateBill(
            @RequestHeader(value = "X-Client-Id", required = true) String clientId, @PathVariable UUID id,
            @RequestBody BillRequest request) {
        Bill updatedBill = billService.updateBill(id, request, clientId);
        BillResponse response = billMapper.toResponse(updatedBill);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a bill by id", description = "Delete a bill by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@RequestHeader(value = "X-Client-Id", required = true) String clientId,
            @PathVariable UUID id) {
        billService.deleteBill(id, clientId);
        return ResponseEntity.noContent().build();
    }

}
