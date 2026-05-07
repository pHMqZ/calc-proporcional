package com.pms.calprop.dto;

import java.math.BigDecimal;

public record BillDistribution(
                Long billId,
                String billDescription,
                Long personId,
                BigDecimal amount,
                BigDecimal percentage) {
}
