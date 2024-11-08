package ru.t1.test;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresSQLTestContainerExtension implements BeforeAllCallback, AfterAllCallback {
    private static final PostgreSQLContainer<?> postgresContainer;

    static {
        postgresContainer = new PostgreSQLContainer<>("postgres:14.0")
                .withDatabaseName("test")
                .withUsername("test")
                .withPassword("test");
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        postgresContainer.start();

        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
        System.setProperty("spring.liquibase.change-log", "classpath:/db.changelog/changelog-test.xml");
        System.setProperty("spring.security.cors.allowedOrigins", "none");
        System.setProperty("spring.security.cors.allowedMethods", "none");
        System.setProperty("spring.security.jwt.secret", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9");
        System.setProperty("image.storage-dir", "./target/test-classes/image-folder");
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {

    }
}
