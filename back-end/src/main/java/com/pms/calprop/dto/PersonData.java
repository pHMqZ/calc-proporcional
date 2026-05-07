package com.pms.calprop.dto;

import java.math.BigDecimal;
import java.util.List;

public record PersonData(
                PersonResponse person,
                BigDecimal percentage,
                BigDecimal reserveAmount,
                BigDecimal billsTotal,
                BigDecimal totalToPay,
                BigDecimal remainingSalary,
                List<BillDistribution> billDistributions) {
}
