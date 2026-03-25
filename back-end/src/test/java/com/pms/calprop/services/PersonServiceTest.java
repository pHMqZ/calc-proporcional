package com.pms.calprop.services;

import com.pms.calprop.entities.Person;
import com.pms.calprop.exceptions.ResourceNotFoundException;
import com.pms.calprop.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
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

    @InjectMocks
    private PersonService personService;

    private Person elis;
    private Person banguela;

    @BeforeEach
    void setUp() {
        elis = new Person();
        elis.setId(1L);
        elis.setName("Elis");
        elis.setSalary(new BigDecimal("3000.00"));
        elis.setReservePercentage(15.0);

        banguela = new Person();
        banguela.setId(2L);
        banguela.setName("Banguela");
        banguela.setSalary(new BigDecimal("5000.00"));
        banguela.setReservePercentage(25.0);
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
        when(personRepository.findAll()).thenReturn(List.of(elis, banguela));

        List<Person> result = personService.findAllPeople();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(personRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return an empty list when there are no people")
    void testFindAllPeopleEmpty() {
        when(personRepository.findAll()).thenReturn(List.of());

        List<Person> result = personService.findAllPeople();

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(personRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find a person by ID")
    void testFindPersonById() {
        when(personRepository.findById(2L)).thenReturn(Optional.of(banguela));

        Person result = personService.findPersonById(2L);

        assertNotNull(result);
        assertEquals("Banguela", result.getName());

        verify(personRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Should throw an exception when trying to find a user that doesn't exist")
    void testNotFoundPersonToFind() {
        when(personRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> personService.findPersonById(99L));

        assertEquals("Pessoa com ID 99 não encontrada!", exception.getMessage());

        verify(personRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Should update user data successfully")
    void testSuccessUpdatedPerson() {
        Person updatedInfo = new Person();
        updatedInfo.setSalary(new BigDecimal("5000.00"));
        updatedInfo.setReservePercentage(25.0);

        when(personRepository.findById(1L)).thenReturn(Optional.of(elis));
        when(personRepository.save(any(Person.class))).thenReturn(elis);

        Person result = personService.updatePerson(1L, updatedInfo);

        assertNotNull(result);
        assertEquals(new BigDecimal("5000.00"), result.getSalary());

        verify(personRepository, times(1)).findById(1L);
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    @DisplayName("Should throw an exception when trying to update a user that doesn't exist")
    void testNotFoundPersonToUpdate() {
        when(personRepository.findById(99L)).thenReturn(Optional.empty());

        Person updatedInfo = new Person();
        updatedInfo.setReservePercentage(30.0);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> personService.updatePerson(99L, updatedInfo));

        assertEquals("Pessoa com ID 99 não encontrada!", exception.getMessage());

        verify(personRepository, never()).save(any(Person.class));

    }

    @Test
    @DisplayName("Should delete a user successfully")
    void testSuccessDeletePerson() {
        when(personRepository.findById(2L)).thenReturn(Optional.of(banguela));

        personService.deletePerson(2L);

        verify(personRepository, times(1)).findById(2L);
        verify(personRepository, times(1)).deleteById(2L);
    }

    @Test
    @DisplayName("Should throw an exception when trying to delete a user that doesn't exist")
    void testNotFoundPersonToDelete() {
        when(personRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> personService.deletePerson(99L));

        assertEquals("Pessoa com ID 99 não encontrada!", exception.getMessage());
        verify(personRepository, never()).deleteById(anyLong());
    }

}
