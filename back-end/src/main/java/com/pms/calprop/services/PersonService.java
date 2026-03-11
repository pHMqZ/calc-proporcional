package com.pms.calprop.services;

import com.pms.calprop.entities.Person;
import com.pms.calprop.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public Person addPerson(Person newPerson) {
        return personRepository.save(newPerson);
    }

    public Person updatePerson(Long id, Person updatePerson) {
        Optional<Person> personOptional = personRepository.findById(id);

        if (personOptional.isPresent()) {
            Person existingPerson = personOptional.get();
            existingPerson.setName(updatePerson.getName());
            existingPerson.setSalary(updatePerson.getSalary());
            existingPerson.setReservePercentage(updatePerson.getReservePercentage());
            return personRepository.save(existingPerson);
        } else {
            throw new RuntimeException("Pessoa com ID " + id + " não encontrada!");
        }
    }

    public void deletePerson (Long id) {
       if(!personRepository.existsById(id)) {
           throw new RuntimeException("Pessoa com ID " + id + " não encontrada!");
       }
       personRepository.deleteById(id);
    }
}
