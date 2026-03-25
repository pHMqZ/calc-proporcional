package com.pms.calprop.dto;

import java.math.BigDecimal;

public record PersonResponse(Long id, String name, BigDecimal salary, BigDecimal reservePercentage) {

}
