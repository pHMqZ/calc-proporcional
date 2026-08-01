package com.pms.calprop.services;

import com.pms.calprop.dto.PersonRequest;
import com.pms.calprop.entities.Person;
import com.pms.calprop.exceptions.ResourceNotFoundException;
import com.pms.calprop.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import org.mapstruct.factory.Mappers;
import com.pms.calprop.mappers.PersonMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Spy
    private PersonMapper personMapper = Mappers.getMapper(PersonMapper.class);

    @InjectMocks
    private PersonService personService;

    private Person elis;
    private Person banguela;

    private final String clientId = "test-client";

    @BeforeEach
    void setUp() {
        elis = new Person();
        elis.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        elis.setName("Elis");
        elis.setSalary(new BigDecimal("3000.00"));
        elis.setReservePercentage(15.0);
        elis.setClientId(clientId);

        banguela = new Person();
        banguela.setId(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        banguela.setName("Banguela");
        banguela.setSalary(new BigDecimal("5000.00"));
        banguela.setReservePercentage(25.0);
        banguela.setClientId(clientId);
    }

    @Test
    @DisplayName("Should save and bring user data")
    void testAddPerson() {
        when(personRepository.save(any(Person.class))).thenReturn(elis);

        Person savedPerson = personService.addPerson(new Person());

        assertNotNull(savedPerson);
        assertEquals("Elis", savedPerson.getName());

        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    @DisplayName("Should return all people")
    void testFindAllPeople() {
        when(personRepository.findByClientId(clientId)).thenReturn(List.of(elis, banguela));

        List<Person> result = personService.findAllPeople(clientId);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(personRepository, times(1)).findByClientId(clientId);
    }

    @Test
    @DisplayName("Should return an empty list when there are no people")
    void testFindAllPeopleEmpty() {
        when(personRepository.findByClientId(clientId)).thenReturn(List.of());

        List<Person> result = personService.findAllPeople(clientId);

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(personRepository, times(1)).findByClientId(clientId);
    }

    @Test
    @DisplayName("Should find a person by ID")
    void testFindPersonById() {
        when(personRepository.findByIdAndClientId(UUID.fromString("00000000-0000-0000-0000-000000000002"), clientId)).thenReturn(Optional.of(banguela));

        Person result = personService.findPersonById(UUID.fromString("00000000-0000-0000-0000-000000000002"), clientId);

        assertNotNull(result);
        assertEquals("Banguela", result.getName());

        verify(personRepository, times(1)).findByIdAndClientId(UUID.fromString("00000000-0000-0000-0000-000000000002"), clientId);
    }

    @Test
    @DisplayName("Should throw an exception when trying to find a user that doesn't exist")
    void testNotFoundPersonToFind() {
        when(personRepository.findByIdAndClientId(UUID.fromString("00000000-0000-0000-0000-000000000099"), clientId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> personService.findPersonById(UUID.fromString("00000000-0000-0000-0000-000000000099"), clientId));

        assertEquals("Participante não encontrado.", exception.getMessage());

        verify(personRepository, times(1)).findByIdAndClientId(UUID.fromString("00000000-0000-0000-0000-000000000099"), clientId);
    }

    @Test
    @DisplayName("Should update user data successfully")
    void testSuccessUpdatedPerson() {

        PersonRequest updatedInfo = new PersonRequest("Elis", new BigDecimal("5000.00"), 20.0);

        when(personRepository.findByIdAndClientId(UUID.fromString("00000000-0000-0000-0000-000000000001"), clientId)).thenReturn(Optional.of(elis));
        when(personRepository.save(any(Person.class))).thenReturn(elis);

        Person result = personService.updatePerson(UUID.fromString("00000000-0000-0000-0000-000000000001"), updatedInfo, clientId);

        assertNotNull(result);
        assertEquals(new BigDecimal("5000.00"), result.getSalary());

        verify(personRepository, times(1)).findByIdAndClientId(UUID.fromString("00000000-0000-0000-0000-000000000001"), clientId);
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    @DisplayName("Should update a person partially successfully, ignoring nulls via MapStruct")
    void testPartialUpdatePerson() {
        when(personRepository.findByIdAndClientId(UUID.fromString("00000000-0000-0000-0000-000000000001"), clientId)).thenReturn(Optional.of(elis));

        PersonRequest requestDTO = new PersonRequest(null, new BigDecimal("5500.00"), null);

        when(personRepository.save(any(Person.class))).thenAnswer(i -> i.getArguments()[0]);

        Person result = personService.updatePerson(UUID.fromString("00000000-0000-0000-0000-000000000001"), requestDTO, clientId);

        assertNotNull(result);
        assertEquals("Elis", result.getName());
        assertEquals(new BigDecimal("5500.00"), result.getSalary());
        assertEquals(15.0, result.getReservePercentage());

        verify(personRepository, times(1)).findByIdAndClientId(UUID.fromString("00000000-0000-0000-0000-000000000001"), clientId);
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    @DisplayName("Should throw an exception when trying to update a user that doesn't exist")
    void testNotFoundPersonToUpdate() {
        when(personRepository.findByIdAndClientId(UUID.fromString("00000000-0000-0000-0000-000000000099"), clientId)).thenReturn(Optional.empty());

        PersonRequest updatedInfo = new PersonRequest("Alceu", new BigDecimal("5000.00"), null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> personService.updatePerson(UUID.fromString("00000000-0000-0000-0000-000000000099"), updatedInfo, clientId));

        assertEquals("Participante não encontrado.", exception.getMessage());

        verify(personRepository, never()).save(any(Person.class));

    }

    @Test
    @DisplayName("Should delete a user successfully")
    void testSuccessDeletePerson() {
        when(personRepository.findByIdAndClientId(UUID.fromString("00000000-0000-0000-0000-000000000002"), clientId)).thenReturn(Optional.of(banguela));

        personService.deletePerson(UUID.fromString("00000000-0000-0000-0000-000000000002"), clientId);

        verify(personRepository, times(1)).findByIdAndClientId(UUID.fromString("00000000-0000-0000-0000-000000000002"), clientId);
        verify(personRepository, times(1)).deleteById(UUID.fromString("00000000-0000-0000-0000-000000000002"));
    }

    @Test
    @DisplayName("Should throw an exception when trying to delete a user that doesn't exist")
    void testNotFoundPersonToDelete() {
        when(personRepository.findByIdAndClientId(UUID.fromString("00000000-0000-0000-0000-000000000099"), clientId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> personService.deletePerson(UUID.fromString("00000000-0000-0000-0000-000000000099"), clientId));

        assertEquals("Participante não encontrado.", exception.getMessage());
        verify(personRepository, never()).deleteById(any(UUID.class));
    }

}
