package com.pms.calprop.services;

import com.pms.calprop.dto.PersonRequest;
import com.pms.calprop.entities.Person;
import com.pms.calprop.exceptions.ResourceNotFoundException;
import com.pms.calprop.mappers.PersonMapper;
import com.pms.calprop.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class PersonService {

    private final PersonRepository personRepository;

    private final PersonMapper personMapper;

    public Person addPerson(Person newPerson) {
        return personRepository.save(newPerson);
    }

    public List<Person> findAllPeople() {
        return personRepository.findAll();
    }

    public Person findPersonById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa com ID " + id + " não encontrada!"));
    }

    public Person updatePerson(Long id, PersonRequest resquest) {

        Person existingPerson = this.findPersonById(id);

        personMapper.updatePersonFromRequest(resquest, existingPerson);
        return personRepository.save(existingPerson);
    }

    public void deletePerson(Long id) {
        this.findPersonById(id);
        personRepository.deleteById(id);
    }

}
