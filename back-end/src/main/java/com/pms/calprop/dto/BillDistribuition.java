package com.pms.calprop.dto;

import java.math.BigDecimal;

public record BillDistribuition(
        Long billId,
        Long personId,
        BigDecimal amount,
        BigDecimal percentage) {
}
