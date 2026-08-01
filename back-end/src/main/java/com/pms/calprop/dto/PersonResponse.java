package com.pms.calprop.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PersonResponse(UUID id, String name, BigDecimal salary, Double reservePercentage) {

}
