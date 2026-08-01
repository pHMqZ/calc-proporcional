package com.pms.calprop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import jakarta.annotation.PostConstruct;

@Configuration
@Profile("prod")
public class ProdDatabaseValidationConfig {

    @Autowired
    private Environment env;

    @PostConstruct
    public void validateDatabase() {
        String driver = env.getProperty("spring.datasource.driver-class-name");
        String url = env.getProperty("spring.datasource.url");

        if (driver != null && driver.toLowerCase().contains("h2")) {
            throw new IllegalStateException("FAIL-FAST: Application is running in 'prod' profile but using H2 driver: " + driver);
        }
        if (url != null && url.toLowerCase().contains("h2")) {
            throw new IllegalStateException("FAIL-FAST: Application is running in 'prod' profile but using H2 URL: " + url);
        }
    }
}
