package com.pms.calprop.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.pms.calprop.dto.BillRequest;
import com.pms.calprop.dto.BillResponse;
import com.pms.calprop.entities.Bill;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BillMapper {

    @Mapping(target = "id", ignore = true)
    Bill toEntity(BillRequest request);

    BillResponse toResponse(Bill bill);

    @Mapping(target = "id", ignore = true)
    void updateBillFromRequest(BillRequest request, @MappingTarget Bill bill);
}
