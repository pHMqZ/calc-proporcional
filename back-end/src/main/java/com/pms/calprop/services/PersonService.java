package com.pms.calprop.services;

import com.pms.calprop.entities.Person;
import com.pms.calprop.exceptions.ResourceNotFoundException;
import com.pms.calprop.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class PersonService {

    private final PersonRepository personRepository;

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

    public Person updatePerson(Long id, Person updatePerson) {

        Person existingPerson = this.findPersonById(id);

        existingPerson.setSalary(updatePerson.getSalary());
        existingPerson.setReservePercentage(updatePerson.getReservePercentage());

        return personRepository.save(existingPerson);
    }

    public void deletePerson(Long id) {
        this.findPersonById(id);
        personRepository.deleteById(id);
    }

}
