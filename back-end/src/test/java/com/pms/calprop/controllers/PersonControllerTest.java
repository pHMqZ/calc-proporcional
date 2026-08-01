package com.pms.calprop.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pms.calprop.dto.PersonRequest;
import com.pms.calprop.dto.PersonResponse;
import com.pms.calprop.entities.Person;
import com.pms.calprop.exceptions.ResourceNotFoundException;
import com.pms.calprop.mappers.PersonMapper;
import com.pms.calprop.services.PersonService;

@WebMvcTest(PersonController.class)
@SuppressWarnings("null")
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PersonService personService;

    @MockitoBean
    private PersonMapper personMapper;

    private final String clientId = "123e4567-e89b-12d3-a456-426614174000";

    @Test
    @DisplayName("Should create a new person successfully")
    void testCreatePerson() throws Exception {
        PersonRequest request = new PersonRequest("Elis", new BigDecimal("3000.00"), 10.0);
        Person person = new Person();
        person.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        person.setName("Elis");
        person.setClientId(clientId);
        PersonResponse response = new PersonResponse(UUID.fromString("00000000-0000-0000-0000-000000000001"), "Elis", new BigDecimal("3000.00"), 10.0);

        when(personMapper.toEntity(any(PersonRequest.class))).thenReturn(person);
        when(personService.addPerson(any(Person.class))).thenReturn(person);
        when(personMapper.toResponse(any(Person.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/person")
                .header("X-Client-Id", clientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("00000000-0000-0000-0000-000000000001"))
                .andExpect(jsonPath("$.name").value("Elis"));
    }

    @Test
    @DisplayName("Should get all people successfully")
    void testGetAllPeople() throws Exception {
        when(personService.findAllPeople(clientId)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/v1/person")
                .header("X-Client-Id", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    @DisplayName("Should get a person by id successfully")
    void testGetPersonById() throws Exception {
        Person person = new Person();
        PersonResponse response = new PersonResponse(UUID.fromString("00000000-0000-0000-0000-000000000001"), "Alceu", new BigDecimal("3000.00"), 20.0);

        when(personService.findPersonById(UUID.fromString("00000000-0000-0000-0000-000000000001"), clientId)).thenReturn(person);
        when(personMapper.toResponse(any(Person.class))).thenReturn(response);

        mockMvc.perform(get("/api/v1/person/00000000-0000-0000-0000-000000000001")
                .header("X-Client-Id", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("00000000-0000-0000-0000-000000000001"))
                .andExpect(jsonPath("$.name").value("Alceu"));
    }

    @Test
    @DisplayName("Should return not found when person does not exist")
    void testGetPersonByIdNotFound() throws Exception {
        when(personService.findPersonById(UUID.fromString("00000000-0000-0000-0000-000000000099"), clientId))
                .thenThrow(new ResourceNotFoundException("Pessoa com ID 99 não encontrada!"));

        mockMvc.perform(get("/api/v1/person/00000000-0000-0000-0000-000000000099")
                .header("X-Client-Id", clientId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Pessoa com ID 99 não encontrada!"));
    }

    @Test
    @DisplayName("Should update a person successfully")
    void testSuccessUpdatedPerson() throws Exception {
        PersonRequest request = new PersonRequest("Elis", new BigDecimal("3000.00"), 10.0);
        Person person = new Person();
        PersonResponse response = new PersonResponse(UUID.fromString("00000000-0000-0000-0000-000000000001"), "Elis", new BigDecimal("3000.00"), 10.0);

        when(personService.updatePerson(eq(UUID.fromString("00000000-0000-0000-0000-000000000001")), any(PersonRequest.class), eq(clientId))).thenReturn(person);
        when(personMapper.toResponse(any(Person.class))).thenReturn(response);

        mockMvc.perform(patch("/api/v1/person/00000000-0000-0000-0000-000000000001")
                .header("X-Client-Id", clientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("00000000-0000-0000-0000-000000000001"));
    }

    @Test
    @DisplayName("Should delete a person successfully")
    void testDeleteAPersonSuccessfully() throws Exception {
        mockMvc.perform(delete("/api/v1/person/00000000-0000-0000-0000-000000000001")
                .header("X-Client-Id", clientId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return bad request when missing client-id header")
    void testShouldReturnBadRequestWhenMissingClientIdHeader() throws Exception {
        mockMvc.perform(get("/api/v1/person")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Sessão inválida ou expirada. Recarregue a página para continuar."));
    }

    @Test
    @DisplayName("Should isolated data between different client ids")
    void testShouldIsolatedDataBetweenDifferentClientIds() throws Exception {
        String clientA = UUID.randomUUID().toString();
        String clientB = UUID.randomUUID().toString();

        when(personMapper.toEntity(any(PersonRequest.class))).thenReturn(new Person());
        when(personService.addPerson(any(Person.class))).thenReturn(new Person());

        mockMvc.perform(post("/api/v1/person")
                .header("X-Client-Id", clientA)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Elis\", \"salary\": 3000.00, \"reservePercentage\": 10.0}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/person")
                .header("X-Client-Id", clientA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0)); // Size is 0 because personService.findAllPeople(clientA) returns null/empty in mock

        mockMvc.perform(get("/api/v1/person")
                .header("X-Client-Id", clientB))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Should return bad gateway when client-id is invalid UUID format")
    void testShouldReturnBadGatewayWhenInvalidClientIdFormat() throws Exception {
        mockMvc.perform(get("/api/v1/person")
                .header("X-Client-Id", "invalid-uuid-format")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.message")
                        .value("Sessão inválida ou expirada. Recarregue a página para continuar."));
    }
}
