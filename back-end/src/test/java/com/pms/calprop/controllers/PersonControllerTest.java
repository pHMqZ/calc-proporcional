package com.pms.calprop.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pms.calprop.dto.PersonRequest;
import com.pms.calprop.entities.Person;
import com.pms.calprop.exceptions.ResourceNotFoundException;
import com.pms.calprop.mappers.PersonMapperImpl;
import com.pms.calprop.services.PersonService;

@WebMvcTest(PersonController.class)
@Import(PersonMapperImpl.class)
@SuppressWarnings("null")
public class PersonControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private PersonService personService;

        @Test
        @DisplayName("Should create a new person successfully")
        void testCreatePerson() throws Exception {

                PersonRequest requestDTO = new PersonRequest("Elis", new BigDecimal("3000.00"), 10.0);

                Person savedPersonMock = new Person();
                savedPersonMock.setId(1L);
                savedPersonMock.setName(requestDTO.name());
                savedPersonMock.setSalary(requestDTO.salary());
                savedPersonMock.setReservePercentage(requestDTO.reservePercentage());

                when(personService.addPerson(any(Person.class))).thenReturn(savedPersonMock);

                mockMvc.perform(post("/api/v1/person")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1L))
                                .andExpect(jsonPath("$.name").value(requestDTO.name()))
                                .andExpect(jsonPath("$.salary").value(requestDTO.salary().doubleValue()))
                                .andExpect(jsonPath("$.reservePercentage").value(requestDTO.reservePercentage()));

        }

        @Test
        @DisplayName("Should get all people successfully")
        void testGetAllPeople() throws Exception {

                PersonRequest toalhaDTO = new PersonRequest("Toalha", new BigDecimal("3000.00"), 10.0);
                PersonRequest bangulaDTO = new PersonRequest("Banguela", new BigDecimal("4000.00"), 15.0);

                Person toalha = new Person();
                toalha.setId(1L);
                toalha.setName(toalhaDTO.name());
                toalha.setSalary(toalhaDTO.salary());
                toalha.setReservePercentage(toalhaDTO.reservePercentage());

                Person banguela = new Person();
                banguela.setId(2L);
                banguela.setName(bangulaDTO.name());
                banguela.setSalary(bangulaDTO.salary());
                banguela.setReservePercentage(bangulaDTO.reservePercentage());

                List<Person> people = Arrays.asList(toalha, banguela);

                when(personService.findAllPeople()).thenReturn(people);

                mockMvc.perform(get("/api/v1/person"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.size()").value(2))
                                .andExpect(jsonPath("$.[0].id").value(1L))
                                .andExpect(jsonPath("$.[0].name").value(toalhaDTO.name()))
                                .andExpect(jsonPath("$.[0].salary").value(toalhaDTO.salary().doubleValue()))
                                .andExpect(jsonPath("$.[0].reservePercentage").value(toalhaDTO.reservePercentage()))
                                .andExpect(jsonPath("$.[1].id").value(2L))
                                .andExpect(jsonPath("$.[1].name").value(bangulaDTO.name()))
                                .andExpect(jsonPath("$.[1].salary").value(bangulaDTO.salary().doubleValue()))
                                .andExpect(jsonPath("$.[1].reservePercentage").value(bangulaDTO.reservePercentage()));

        }

        @Test
        @DisplayName("Should return a empty list when there are no people")
        void testGetAllPeopleEmpty() throws Exception {
                when(personService.findAllPeople()).thenReturn(new ArrayList<>());
                mockMvc.perform(get("/api/v1/person"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.size()").value(0));
        }

        @Test
        @DisplayName("Should get a person by id successfully")
        void testGetPersonById() throws Exception {

                PersonRequest alceuDTO = new PersonRequest("Alceu", new BigDecimal("3000.00"), 20.0);
                Person alceu = new Person();
                alceu.setId(1L);
                alceu.setName(alceuDTO.name());
                alceu.setSalary(alceuDTO.salary());
                alceu.setReservePercentage(alceuDTO.reservePercentage());

                when(personService.findPersonById(1L)).thenReturn(alceu);

                mockMvc.perform(get("/api/v1/person/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1L))
                                .andExpect(jsonPath("$.name").value(alceuDTO.name()))
                                .andExpect(jsonPath("$.salary").value(alceuDTO.salary().doubleValue()))
                                .andExpect(jsonPath("$.reservePercentage").value(alceuDTO.reservePercentage()));

        }

        @Test
        @DisplayName("Should return not found when person does not exist")
        void testGetPersonByIdNotFound() throws Exception {
                when(personService.findPersonById(99L))
                                .thenThrow(new ResourceNotFoundException("Pessoa com ID 99 não encontrada!"));

                mockMvc.perform(get("/api/v1/person/99"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("Pessoa com ID 99 não encontrada!"));
        }

        @Test
        @DisplayName("Should update a person successfully")
        void testSuccessUpdatedPerson() throws Exception {

                PersonRequest updatedInfo = new PersonRequest("Elis", new BigDecimal("3000.00"), 10.0);

                Person savedPersonMock = new Person();
                savedPersonMock.setId(1L);
                savedPersonMock.setName(updatedInfo.name());
                savedPersonMock.setSalary(updatedInfo.salary());
                savedPersonMock.setReservePercentage(updatedInfo.reservePercentage());

                when(personService.updatePerson(eq(1L), any(PersonRequest.class))).thenReturn(savedPersonMock);

                mockMvc.perform(patch("/api/v1/person/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedInfo)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1L))
                                .andExpect(jsonPath("$.name").value(updatedInfo.name()))
                                .andExpect(jsonPath("$.salary").value(updatedInfo.salary().doubleValue()))
                                .andExpect(jsonPath("$.reservePercentage").value(updatedInfo.reservePercentage()));

        }

        @Test
        @DisplayName("Should throw a exception when person not found")
        void testThrowExceptionWhenUpdatingPersonNotFound() throws Exception {
                PersonRequest updatedInfo = new PersonRequest("Elis", new BigDecimal("3000.00"), 10.0);

                when(personService.updatePerson(eq(99L), any(PersonRequest.class)))
                                .thenThrow(new ResourceNotFoundException("Pessoa com ID 99 não encontrada!"));

                mockMvc.perform(patch("/api/v1/person/99")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedInfo)))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("Pessoa com ID 99 não encontrada!"));
        }

        @Test
        @DisplayName("Should delete a person successfully")
        void testDeleteAPersonSuccessfully() throws Exception {
                mockMvc.perform(delete("/api/v1/person/1"))
                                .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Should throw a exception when person not found")
        void testThrowExceptionWhenDeletingPersonNotFound() throws Exception {

                Mockito.doThrow(new ResourceNotFoundException("Pessoa com ID 99 não encontrada!")).when(personService)
                                .deletePerson(99L);

                mockMvc.perform(delete("/api/v1/person/99"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("Pessoa com ID 99 não encontrada!"));
        }
}
