package com.pms.calprop.dto;

import java.math.BigDecimal;

public record BillRequest(String description, BigDecimal totalAmount) {

}
