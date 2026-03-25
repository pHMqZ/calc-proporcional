package com.pms.calprop.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pms.calprop.dto.PersonRequest;
import com.pms.calprop.dto.PersonResponse;
import com.pms.calprop.entities.Person;
import com.pms.calprop.exceptions.ResourceNotFoundException;
import com.pms.calprop.mappers.PersonMapper;
import com.pms.calprop.services.PersonService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/person")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    private final PersonMapper personMapper;

    @PostMapping
    public ResponseEntity<PersonResponse> addPerson(@RequestBody PersonRequest request) {
        Person person = personMapper.toEntity(request);

        Person savedPerson = personService.addPerson(person);

        PersonResponse response = personMapper.toResponse(savedPerson);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PersonResponse>> getAllPeople() {
        List<Person> people = personService.findAllPeople();
        List<PersonResponse> responses = people.stream()
                .map(person -> personMapper.toResponse(person))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> getPersonById(@PathVariable Long id) {
        Person person = personService.findPersonById(id);
        PersonResponse response = personMapper.toResponse(person);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PersonResponse> updatePerson(@PathVariable Long id, @RequestBody PersonRequest request) {

        Person updatedPerson = personService.updatePerson(id, request);

        PersonResponse response = personMapper.toResponse(updatedPerson);

        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFound(ResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", exception.getMessage()));
    }

}
