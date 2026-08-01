package com.pms.calprop.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record BillDistribution(
                UUID billId,
                String billDescription,
                UUID personId,
                BigDecimal amount,
                BigDecimal percentage) {
}
