package com.pms.calprop.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.pms.calprop.dto.PersonRequest;
import com.pms.calprop.dto.PersonResponse;
import com.pms.calprop.entities.Person;

class PersonMapperTest {

    private PersonMapper personMapper;

    @BeforeEach
    void setUp() {
        personMapper = Mappers.getMapper(PersonMapper.class);
    }

    @Test
    @DisplayName("successfully convert the Person entity to PersonResponse.")
    void shouldMapPersonToPersonResponse() {

        Person person = new Person("Alceu", new BigDecimal("4500.00"), 10.0, "test-client");
        person.setId(1L);

        PersonResponse response = personMapper.toResponse(person);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(person.getId());
        assertThat(response.name()).isEqualTo(person.getName());
        assertThat(response.salary()).isEqualByComparingTo(person.getSalary());
        assertThat(response.reservePercentage()).isEqualTo(person.getReservePercentage());
    }

    @Test
    @DisplayName("Should convert PersonRequest to Person Entity and ignore the ID.")
    void shouldMapPersonRequestToPerson() {

        PersonRequest request = new PersonRequest("Alceu", new BigDecimal("6000.00"), 15.0);

        Person person = personMapper.toEntity(request);

        assertThat(person).isNotNull();
        assertThat(person.getId()).isNull();
        assertThat(person.getName()).isEqualTo(request.name());
        assertThat(person.getSalary()).isEqualByComparingTo(request.salary());
        assertThat(person.getReservePercentage()).isEqualTo(request.reservePercentage());
    }

    @Test
    @DisplayName("Should update Person entity from PersonRequest preservando the ID.")
    void shouldUpdatePersonFromRequest() {

        Long originalId = 99L;
        Person existingPerson = new Person("Alceu", new BigDecimal("2000.00"), 5.0, "test-client");
        existingPerson.setId(originalId);

        PersonRequest updateRequest = new PersonRequest("Elis", new BigDecimal("3500.00"), 20.0);

        personMapper.updatePersonFromRequest(updateRequest, existingPerson);
        assertThat(existingPerson.getId()).isEqualTo(originalId);
        assertThat(existingPerson.getName()).isEqualTo(updateRequest.name());
        assertThat(existingPerson.getSalary()).isEqualByComparingTo(updateRequest.salary());
        assertThat(existingPerson.getReservePercentage()).isEqualTo(updateRequest.reservePercentage());
    }
}
