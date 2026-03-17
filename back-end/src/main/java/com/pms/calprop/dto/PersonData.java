package com.pms.calprop.dto;

import java.math.BigDecimal;
import java.util.List;

import com.pms.calprop.entities.Person;

public record PersonData(
        Person person,
        BigDecimal percentage,
        BigDecimal reserveAmount,
        BigDecimal billsTotal,
        BigDecimal totalToPay,
        BigDecimal remainingSalary,
        List<BillDistribution> billDistributions) {
}
