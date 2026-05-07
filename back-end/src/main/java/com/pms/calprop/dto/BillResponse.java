package com.pms.calprop.dto;

import java.math.BigDecimal;

public record BillResponse(Long id, String description, BigDecimal totalAmount) {

}
