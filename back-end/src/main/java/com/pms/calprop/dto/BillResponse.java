package com.pms.calprop.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record BillResponse(UUID id, String description, BigDecimal totalAmount) {

}
