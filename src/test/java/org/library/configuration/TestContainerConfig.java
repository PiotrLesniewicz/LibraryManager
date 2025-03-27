package org.library.configuration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class TestContainerConfig {

    @Container
    private final static PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:17.4");

    static {
        POSTGRES_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    public static void configurePostgresProperty(DynamicPropertyRegistry registry) {
        registry.add("jdbc.url", POSTGRES_SQL_CONTAINER::getJdbcUrl);
        registry.add("jdbc.user", POSTGRES_SQL_CONTAINER::getUsername);
        registry.add("jdbc.pass", POSTGRES_SQL_CONTAINER::getPassword);
    }
}
