package com.pms.calprop.services;

import com.pms.calprop.entities.Person;
import com.pms.calprop.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class PersonService {

    private final PersonRepository personRepository;

    public Person addPerson(Person newPerson) {
        return personRepository.save(newPerson);
    }

    public Person updatePerson(Long id, Person updatePerson) {

        Person existingPerson = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pessoa com ID " + id + " não encontrada!"));

        existingPerson.setSalary(updatePerson.getSalary());
        existingPerson.setReservePercentage(updatePerson.getReservePercentage());

        return personRepository.save(existingPerson);
    }

    public void deletePerson(Long id) {
        if (!personRepository.existsById(id)) {
            throw new RuntimeException("Pessoa com ID " + id + " não encontrada!");
        }
        personRepository.deleteById(id);
    }
}
