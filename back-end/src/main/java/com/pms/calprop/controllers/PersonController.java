package com.pms.calprop.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pms.calprop.dto.PersonRequest;
import com.pms.calprop.dto.PersonResponse;
import com.pms.calprop.entities.Person;
import com.pms.calprop.mappers.PersonMapper;
import com.pms.calprop.services.PersonService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/person")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    private final PersonMapper personMapper;

    @Operation(summary = "Add a new person in calculation", description = "Need a name, salary and percentage for reserve of the person")
    @PostMapping
    public ResponseEntity<PersonResponse> addPerson(
            @RequestHeader(value = "X-Client-Id", required = true) String clientId,
            @RequestBody PersonRequest request) {
        Person person = personMapper.toEntity(request);
        person.setClientId(clientId);

        Person savedPerson = personService.addPerson(person);

        PersonResponse response = personMapper.toResponse(savedPerson);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all people", description = "Return all registered people")
    @GetMapping
    public ResponseEntity<List<PersonResponse>> getAllPeople(@RequestHeader("X-Client-Id") String clientId) {
        List<Person> people = personService.findAllPeople(clientId);
        List<PersonResponse> responses = people.stream()
                .map(person -> personMapper.toResponse(person))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get a person by id", description = "Return a person by id")
    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> getPersonById(
            @RequestHeader(value = "X-Client-Id", required = true) String clientId, @PathVariable UUID id) {
        Person person = personService.findPersonById(id, clientId);
        PersonResponse response = personMapper.toResponse(person);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update a person by id", description = "Provide only the fields you want to change in the request. Null fields will be ignored and kept intact in the database.")
    @PatchMapping("/{id}")
    public ResponseEntity<PersonResponse> updatePerson(
            @PathVariable UUID id,
            @RequestHeader(value = "X-Client-Id", required = true) String clientId,
            @RequestBody PersonRequest request) {

        Person updatedPerson = personService.updatePerson(id, request, clientId);
        PersonResponse response = personMapper.toResponse(updatedPerson);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a person by id", description = "Delete a person by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(
            @PathVariable UUID id,
            @RequestHeader(value = "X-Client-Id", required = true) String clientId) {
        personService.deletePerson(id, clientId);
        return ResponseEntity.noContent().build();
    }

}
