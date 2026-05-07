package com.pms.calprop.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pms.calprop.dto.CalculationSummaryResponse;
import com.pms.calprop.services.BillService;
import com.pms.calprop.services.CalculationService;
import com.pms.calprop.services.PersonService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/calculation")
@RequiredArgsConstructor
public class CalculationController {

    private final PersonService personService;
    private final BillService billService;
    private final CalculationService calculationService;

    @Operation(summary = "Get the full calculation summary", description = "Return the full calculation summary")
    @GetMapping
    public ResponseEntity<CalculationSummaryResponse> getSummary() {
        var people = personService.findAllPeople();
        var bills = billService.findAllBills();
        var summary = calculationService.calculateSummaryData(bills, people);
        return ResponseEntity.ok(summary);
    }
}
