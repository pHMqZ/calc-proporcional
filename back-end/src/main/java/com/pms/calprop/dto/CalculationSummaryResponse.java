package com.pms.calprop.dto;

import java.math.BigDecimal;
import java.util.List;

public record CalculationSummaryResponse(
                BigDecimal totalSalary,
                BigDecimal totalBills,
                BigDecimal totalWithReserve,
                BigDecimal totalRemainder,
                List<PersonData> peopleData,
                List<BillDistribution> billsDistribution) {
}