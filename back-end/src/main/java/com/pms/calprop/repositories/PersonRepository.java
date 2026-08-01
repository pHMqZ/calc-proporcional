package com.pms.calprop.repositories;

import com.pms.calprop.entities.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, UUID> {

    List<Person> findByClientId(String clientId);

    Optional<Person> findByIdAndClientId(UUID id, String clientId);

}
