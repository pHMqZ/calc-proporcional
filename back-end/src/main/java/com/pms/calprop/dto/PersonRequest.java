package com.pms.calprop.dto;

import java.math.BigDecimal;

public record PersonRequest(String name, BigDecimal salary, BigDecimal reservePercentage) {

}
