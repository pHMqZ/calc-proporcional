package com.pms.calprop.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.pms.calprop.entities.Person;
import com.pms.calprop.repositories.PersonRepository;

import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import org.junit.jupiter.api.Disabled;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test") // Ensure it doesn't conflict with prod profile
@Disabled("Requires Docker, run in CI")
public class UuidIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("calprop_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.flyway.enabled", () -> "true");
        // Let Flyway create the schema, then validate
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
    }

    @Autowired
    private PersonRepository personRepository;

    @Test
    void shouldGenerateRandomUuidv4AndSaveSuccessfully() {
        Person person = new Person("John Doe", new BigDecimal("5000.00"), 10.0, "client-123");
        Person savedPerson = personRepository.saveAndFlush(person);

        assertThat(savedPerson.getId()).isNotNull();
        // Check if it's a UUID v4
        assertThat(savedPerson.getId().version()).isEqualTo(4);
    }

    @Test
    void shouldRejectConstraintViolationsWithDataIntegrityViolationException() {
        // Violating the CHECK (salary >= 0) constraint from the database
        Person invalidPerson = new Person("Jane Doe", new BigDecimal("-1000.00"), 10.0, "client-123");
        
        assertThrows(DataIntegrityViolationException.class, () -> {
            personRepository.saveAndFlush(invalidPerson);
        });
    }

    @Test
    void shouldGenerateNonPredictableUuids() {
        List<UUID> generatedIds = new ArrayList<>();
        
        for (int i = 0; i < 10; i++) {
            Person p = new Person("Person " + i, new BigDecimal("1000.00"), 10.0, "client-123");
            Person saved = personRepository.saveAndFlush(p);
            generatedIds.add(saved.getId());
        }

        // Assert all generated IDs are distinct
        long uniqueCount = generatedIds.stream().distinct().count();
        assertThat(uniqueCount).isEqualTo(10);

        // Assert all are UUID v4 (purely random, no timestamp/MAC address)
        for (UUID id : generatedIds) {
            assertThat(id.version()).isEqualTo(4);
        }
    }
}
