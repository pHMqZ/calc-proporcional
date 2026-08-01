package com.pms.calprop.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProdDatabaseValidationConfigTest {

    @Test
    void shouldFailIfH2IsUsedInProd() {
        Exception exception = assertThrows(Exception.class, () -> {
            ConfigurableApplicationContext ctx = SpringApplication.run(com.pms.calprop.CalpropApplication.class,
                "--spring.profiles.active=prod",
                "--spring.datasource.url=jdbc:h2:mem:test",
                "--spring.datasource.driver-class-name=org.h2.Driver",
                "--spring.jpa.hibernate.ddl-auto=none",
                "--spring.flyway.enabled=false",
                "--server.port=0"
            );
            if (ctx != null) {
                ctx.close();
            }
        });

        // O erro real estará encapsulado (BeanCreationException -> IllegalStateException)
        assertThat(exception.getCause().getMessage()).contains("FAIL-FAST");
    }
}
