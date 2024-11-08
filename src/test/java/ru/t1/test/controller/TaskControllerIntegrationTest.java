package ru.t1.test.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.t1.test.PostgresSQLTestContainerExtension;
import ru.t1.test.dto.TaskDto;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.t1.test.util.TestData.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(scripts = "classpath:testdata/add_task_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:testdata/clear_task_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
})
class TaskControllerIntegrationTest extends PostgresSQLTestContainerExtension {

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void get() {
        ResponseEntity<TaskDto> responseEntity = restTemplate.exchange(
                URL_TEMPLATE,
                HttpMethod.GET,
                null,
                TaskDto.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(TASK_DTO_1, responseEntity.getBody());
    }

    @Test
    void getAll() {
        ResponseEntity<List<TaskDto>> responseEntity = restTemplate.exchange(
                URL_TEMPLATE_TASKS,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<>() {});
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody().size()).isEqualTo(TASK_DTO_LIST.size());
        Assertions.assertThat(responseEntity.getBody())
                .usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(TASK_DTO_LIST);
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