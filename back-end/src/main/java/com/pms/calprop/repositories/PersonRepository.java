package com.pms.calprop.repositories;

import com.pms.calprop.entities.Person;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByClientId(String clientId);

    Optional<Person> findByIdAndClientId(Long id, String clientId);

}
