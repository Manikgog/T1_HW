package ru.t1.test.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import ru.t1.test.PostgresSQLTestContainerExtension;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(PostgresSQLTestContainerExtension.class)
class TaskControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void get() {
    }

    @Test
    void getAll() {
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}