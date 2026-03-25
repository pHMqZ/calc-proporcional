package com.pms.calprop.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.pms.calprop.dto.PersonRequest;
import com.pms.calprop.dto.PersonResponse;
import com.pms.calprop.entities.Person;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PersonMapper {

    @Mapping(target = "id", ignore = true)
    Person toEntity(PersonRequest request);

    PersonResponse toResponse(Person person);

    @Mapping(target = "id", ignore = true)
    void updatePersonFromRequest(PersonRequest request, @MappingTarget Person person);
}
