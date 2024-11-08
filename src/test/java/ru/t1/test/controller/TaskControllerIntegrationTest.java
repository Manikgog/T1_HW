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
import ru.t1.test.repository.TaskRepository;
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

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void getPositiveTest() {
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
    void getNegativeTestById() {
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                String.format("/tasks/%s", WRONG_TASK_ID),
                HttpMethod.GET,
                null,
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assertions.assertThat(responseEntity.getBody()).isEqualToIgnoringCase("Task not found");
    }


    @Test
    void getAllTest() {
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
    void createPositiveTest() {
        taskRepository.deleteAll();
        TaskDto taskDto = new TaskDto("test title 100", "test description 100", "running");
        HttpEntity<TaskDto> requestEntity = new HttpEntity<>(taskDto);
        ResponseEntity<Integer> responseEntity = restTemplate.exchange(
                URL_TEMPLATE_TASKS,
                HttpMethod.POST,
                requestEntity,
                Integer.class
        );
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(1);
    }

    @Test
    void createNegativeTestByEmptyTitle() {
        TaskDto taskDto = new TaskDto("", "test description 100", "running");
        HttpEntity<TaskDto> requestEntity = new HttpEntity<>(taskDto);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                URL_TEMPLATE_TASKS,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(responseEntity.getBody()).isEqualToIgnoringCase("Title is empty. ");
    }


    @Test
    void createNegativeTestByBlankTitle() {
        TaskDto taskDto = new TaskDto("  ", "test description 100", "running");
        HttpEntity<TaskDto> requestEntity = new HttpEntity<>(taskDto);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                URL_TEMPLATE_TASKS,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(responseEntity.getBody()).isEqualToIgnoringCase("Title is empty. ");
    }


    @Test
    void createNegativeTestByEmptyDescription() {
        TaskDto taskDto = new TaskDto("test title 100", "", "running");
        HttpEntity<TaskDto> requestEntity = new HttpEntity<>(taskDto);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                URL_TEMPLATE_TASKS,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(responseEntity.getBody()).isEqualToIgnoringCase("Description is empty. ");
    }


    @Test
    void createNegativeTestByBlankDescription() {
        TaskDto taskDto = new TaskDto("test title 100", "  ", "running");
        HttpEntity<TaskDto> requestEntity = new HttpEntity<>(taskDto);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                URL_TEMPLATE_TASKS,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(responseEntity.getBody()).isEqualToIgnoringCase("Description is empty. ");
    }


    @Test
    void createNegativeTestByWrongStatus() {
        TaskDto taskDto = new TaskDto("test title 100", "test description 100", "wrong status");
        HttpEntity<TaskDto> requestEntity = new HttpEntity<>(taskDto);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                URL_TEMPLATE_TASKS,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(responseEntity.getBody()).isEqualToIgnoringCase("Status is incorrect.");
    }


    @Test
    void createNegativeTestByBlankStatus() {
        TaskDto taskDto = new TaskDto("test title 100", "test description 100", "  ");
        HttpEntity<TaskDto> requestEntity = new HttpEntity<>(taskDto);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                URL_TEMPLATE_TASKS,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(responseEntity.getBody()).isEqualToIgnoringCase("Status is empty. Status is incorrect.");
    }


    @Test
    void createNegativeTestByEmptyStatus() {
        TaskDto taskDto = new TaskDto("test title 100", "test description 100", "");
        HttpEntity<TaskDto> requestEntity = new HttpEntity<>(taskDto);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                URL_TEMPLATE_TASKS,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(responseEntity.getBody()).isEqualToIgnoringCase("Status is empty. Status is incorrect.");
    }


    @Test
    void updatePositiveTest() {
        HttpEntity<TaskDto> requestEntity = new HttpEntity<>(TASK_DTO_1_COMPLETED);
        ResponseEntity<TaskDto> responseEntity = restTemplate.exchange(
                String.format("/tasks/%s", TASK_ID),
                HttpMethod.PUT,
                requestEntity,
                TaskDto.class
        );
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(TASK_DTO_1_COMPLETED, responseEntity.getBody());
    }

    @Test
    void updateNegativeTestById() {
        HttpEntity<TaskDto> requestEntity = new HttpEntity<>(TASK_DTO_1_COMPLETED);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                String.format("/tasks/%s", WRONG_TASK_ID),
                HttpMethod.PUT,
                requestEntity,
                String.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assertions.assertThat(responseEntity.getBody()).isEqualToIgnoringCase("Task not found");
    }

    @Test
    void deletePositiveTest() {
        ResponseEntity<TaskDto> responseEntity = restTemplate.exchange(
                URL_TEMPLATE,
                HttpMethod.DELETE,
                null,
                TaskDto.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(TASK_DTO_1, responseEntity.getBody());
    }


    @Test
    void deleteNegativeTest() {
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                String.format("/tasks/%s", WRONG_TASK_ID),
                HttpMethod.DELETE,
                null,
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assertions.assertThat(responseEntity.getBody()).isEqualToIgnoringCase("Task not found");
    }
}