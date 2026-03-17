package com.pms.calprop.services;

import com.pms.calprop.entities.Person;
import com.pms.calprop.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person();
        person.setId(1L);
        person.setName("Elis");
        person.setSalary(new BigDecimal("3000.00"));
        person.setReservePercentage(15.0);
    }

    @Test
    @DisplayName("Should save and bring user data")
    void testAddPerson() {
        when(personRepository.save(any(Person.class))).thenReturn(person);

        Person savedPerson = personService.addPerson(new Person());

        assertNotNull(savedPerson);
        assertEquals("Elis", savedPerson.getName());

        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    @DisplayName("Should update user data successfully")
    void testSuccessUpdatedPerson() {
        Person updatedInfo = new Person();
        updatedInfo.setSalary(new BigDecimal("5000.00"));
        updatedInfo.setReservePercentage(25.0);

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personRepository.save(any(Person.class))).thenReturn(person);

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

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> personService.updatePerson(99L, updatedInfo));

        assertEquals("Pessoa com ID 99 não encontrada!", exception.getMessage());

        verify(personRepository, never()).save(any(Person.class));

    }

    @Test
    @DisplayName("Should delete a user successfully")
    void testSuccessDeletePerson() {
        when(personRepository.existsById(1L)).thenReturn(true);

        personService.deletePerson(1L);

        verify(personRepository, times(1)).existsById(1L);
        verify(personRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw an exception when trying to delete a user that doesn't exist")
    void testNotFoundPersonToDelete() {
        when(personRepository.existsById(99L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> personService.deletePerson(99L));

        assertEquals("Pessoa com ID 99 não encontrada!", exception.getMessage());
        verify(personRepository, never()).deleteById(anyLong());
    }

}
