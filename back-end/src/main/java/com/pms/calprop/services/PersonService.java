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

    public List<Person> findAllPeople(String clientId) {
        return personRepository.findByClientId(clientId);
    }

    public Person findPersonById(Long id, String clientId) {
        return personRepository.findByIdAndClientId(id, clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Participante não encontrado."));
    }

    public Person updatePerson(Long id, PersonRequest resquest, String clientId) {

        Person existingPerson = this.findPersonById(id, clientId);

        personMapper.updatePersonFromRequest(resquest, existingPerson);
        return personRepository.save(existingPerson);
    }

    public void deletePerson(Long id, String clientId) {
        this.findPersonById(id, clientId);
        personRepository.deleteById(id);
    }

}
