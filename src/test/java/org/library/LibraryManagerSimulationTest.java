package org.library;

import org.junit.jupiter.api.Test;
import org.library.infrastructure.configuration.ApplicationConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringJUnitConfig(classes = {ApplicationConfiguration.class})
public class LibraryManagerSimulationTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");


    @DynamicPropertySource
    static void postgresSqlProperty(DynamicPropertyRegistry registry) {
        registry.add("jdbc.url", postgreSQLContainer::getJdbcUrl);
        registry.add("jdbc.user", postgreSQLContainer::getUsername);
        registry.add("jdbc.pass", postgreSQLContainer::getPassword);
    }

    @Test
    void test() {
    }
}
