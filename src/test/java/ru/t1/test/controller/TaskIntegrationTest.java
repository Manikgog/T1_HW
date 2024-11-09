package ru.t1.test.controller;

import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.t1.test.PostgresSQLTestContainerExtension;
import ru.t1.test.TaskStatus;
import ru.t1.test.dto.TaskDto;
import ru.t1.test.entity.Task;
import ru.t1.test.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.t1.test.util.TestData.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
/*@SqlGroup({
        @Sql(scripts = "classpath:testdata/add_task_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:testdata/clear_task_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
})*/
public class TaskIntegrationTest extends PostgresSQLTestContainerExtension {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.saveAll(List.of(TASK_1, TASK_2, TASK_3, TASK_4));
    }

    @AfterEach
    void tearDown() {
        taskRepository.deleteAll();
    }

    @Test
    void getPositiveTest() throws Exception {
        mockMvc.perform(get(URL_TEMPLATE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(TASK_1.getTitle())))
                .andExpect(jsonPath("$.description", is(TASK_1.getDescription())))
                .andExpect(jsonPath("$.status", is(TaskStatus.RUNNING.toString())));
    }

    @Test
    void getNegativeTest() throws Exception {
        mockMvc.perform(get(String.format("/tasks/%s", WRONG_TASK_ID)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postPositiveTest() throws Exception {
        taskRepository.deleteAll();
        TaskDto taskDto = TASK_DTO_5;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", taskDto.title());
        jsonObject.put("description", taskDto.description());
        jsonObject.put("status", taskDto.status());

        mockMvc.perform(post("/tasks")
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(String.valueOf(TASK_ID)));

        Optional<Task> task = taskRepository.findById(TASK_ID);
        Assertions.assertThat(task.isPresent()).isTrue();
        Assertions.assertThat(task.get().getTitle()).isEqualTo(TASK_DTO_5.title());
        Assertions.assertThat(task.get().getDescription()).isEqualTo(TASK_DTO_5.description());
        Assertions.assertThat(task.get().getStatus()).isEqualTo(TaskStatus.RUNNING);
    }

    @Test
    void postNegativeTestByEmptyTitle() throws Exception {
        TaskDto taskDto = TASK_DTO_5;
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("title", "");
        facultyObject.put("description", taskDto.description());
        facultyObject.put("status", taskDto.status());

        mockMvc.perform(post("/tasks")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Title is empty. "));
    }

    @Test
    void postNegativeTestByBlankTitle() throws Exception {
        TaskDto taskDto = TASK_DTO_5;
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("title", "  ");
        facultyObject.put("description", taskDto.description());
        facultyObject.put("status", taskDto.status());

        mockMvc.perform(post("/tasks")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Title is empty. "));
    }

    @Test
    void postNegativeTestByEmptyDescription() throws Exception {
        TaskDto taskDto = TASK_DTO_5;
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("title", taskDto.title());
        facultyObject.put("description", "");
        facultyObject.put("status", taskDto.status());

        mockMvc.perform(post("/tasks")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Description is empty. "));
    }

    @Test
    void postNegativeTestByBlankDescription() throws Exception {
        TaskDto taskDto = TASK_DTO_5;
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("title", taskDto.title());
        facultyObject.put("description", "  ");
        facultyObject.put("status", taskDto.status());

        mockMvc.perform(post("/tasks")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Description is empty. "));
    }

    @Test
    void postNegativeTestByWrongStatus() throws Exception {
        TaskDto taskDto = TASK_DTO_5;
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("title", taskDto.title());
        facultyObject.put("description", taskDto.description());
        facultyObject.put("status", "wrong status");

        mockMvc.perform(post("/tasks")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Status is incorrect."));
    }

    @Test
    void postNegativeTestByBlankStatus() throws Exception {
        TaskDto taskDto = TASK_DTO_5;
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("title", taskDto.title());
        facultyObject.put("description", taskDto.description());
        facultyObject.put("status", "   ");

        mockMvc.perform(post("/tasks")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Status is empty. Status is incorrect."));
    }


    @Test
    void postNegativeTestByEmptyStatus() throws Exception {
        TaskDto taskDto = TASK_DTO_5;
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("title", taskDto.title());
        facultyObject.put("description", taskDto.description());
        facultyObject.put("status", "");

        mockMvc.perform(post("/tasks")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Status is empty. Status is incorrect."));
    }

    @Test
    void updatePositiveTest() throws Exception {
        TaskDto taskDto = TASK_DTO_1_COMPLETED;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", taskDto.title());
        jsonObject.put("description", taskDto.description());
        jsonObject.put("status", taskDto.status());

        mockMvc.perform(put(URL_TEMPLATE)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(TASK_1.getTitle())))
                .andExpect(jsonPath("$.description", is(TASK_1.getDescription())))
                .andExpect(jsonPath("$.status", is(TaskStatus.COMPLETED.toString())));

        Optional<Task> task = taskRepository.findById(TASK_ID);
        Assertions.assertThat(task.isPresent()).isTrue();
        Assertions.assertThat(task.get().getTitle()).isEqualTo(TASK_DTO_1_COMPLETED.title());
        Assertions.assertThat(task.get().getDescription()).isEqualTo(TASK_DTO_1_COMPLETED.description());
        Assertions.assertThat(task.get().getStatus()).isEqualTo(TaskStatus.COMPLETED);
    }

    @Test
    void updateNegativeTestByEmptyTitle() throws Exception {
        TaskDto taskDto = TASK_DTO_1_COMPLETED;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "");
        jsonObject.put("description", taskDto.description());
        jsonObject.put("status", taskDto.status());

        mockMvc.perform(put(URL_TEMPLATE)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Optional<Task> task = taskRepository.findById(TASK_ID);
        Assertions.assertThat(task.isPresent()).isTrue();
        Assertions.assertThat(task.get().getTitle()).isEqualTo(TASK_DTO_1.title());
        Assertions.assertThat(task.get().getDescription()).isEqualTo(TASK_DTO_1.description());
        Assertions.assertThat(task.get().getStatus()).isEqualTo(TaskStatus.RUNNING);
    }

    @Test
    void updateNegativeTestByBlankTitle() throws Exception {
        TaskDto taskDto = TASK_DTO_1_COMPLETED;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "  ");
        jsonObject.put("description", taskDto.description());
        jsonObject.put("status", taskDto.status());

        mockMvc.perform(put(URL_TEMPLATE)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Optional<Task> task = taskRepository.findById(TASK_ID);
        Assertions.assertThat(task.isPresent()).isTrue();
        Assertions.assertThat(task.get().getTitle()).isEqualTo(TASK_DTO_1.title());
        Assertions.assertThat(task.get().getDescription()).isEqualTo(TASK_DTO_1.description());
        Assertions.assertThat(task.get().getStatus()).isEqualTo(TaskStatus.RUNNING);
    }

    @Test
    void updateNegativeTestByEmptyDescription() throws Exception {
        TaskDto taskDto = TASK_DTO_1_COMPLETED;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", taskDto.title());
        jsonObject.put("description", "");
        jsonObject.put("status", taskDto.status());

        mockMvc.perform(put(URL_TEMPLATE)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Optional<Task> task = taskRepository.findById(TASK_ID);
        Assertions.assertThat(task.isPresent()).isTrue();
        Assertions.assertThat(task.get().getTitle()).isEqualTo(TASK_DTO_1.title());
        Assertions.assertThat(task.get().getDescription()).isEqualTo(TASK_DTO_1.description());
        Assertions.assertThat(task.get().getStatus()).isEqualTo(TaskStatus.RUNNING);
    }

    @Test
    void updateNegativeTestByBlankDescription() throws Exception {
        TaskDto taskDto = TASK_DTO_1_COMPLETED;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", taskDto.title());
        jsonObject.put("description", "  ");
        jsonObject.put("status", taskDto.status());

        mockMvc.perform(put(URL_TEMPLATE)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Optional<Task> task = taskRepository.findById(TASK_ID);
        Assertions.assertThat(task.isPresent()).isTrue();
        Assertions.assertThat(task.get().getTitle()).isEqualTo(TASK_DTO_1.title());
        Assertions.assertThat(task.get().getDescription()).isEqualTo(TASK_DTO_1.description());
        Assertions.assertThat(task.get().getStatus()).isEqualTo(TaskStatus.RUNNING);
    }

    @Test
    void updateNegativeTestByEmptyStatus() throws Exception {
        TaskDto taskDto = TASK_DTO_1_COMPLETED;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", taskDto.title());
        jsonObject.put("description", taskDto.description());
        jsonObject.put("status", "");

        mockMvc.perform(put(URL_TEMPLATE)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Optional<Task> task = taskRepository.findById(TASK_ID);
        Assertions.assertThat(task.isPresent()).isTrue();
        Assertions.assertThat(task.get().getTitle()).isEqualTo(TASK_DTO_1.title());
        Assertions.assertThat(task.get().getDescription()).isEqualTo(TASK_DTO_1.description());
        Assertions.assertThat(task.get().getStatus()).isEqualTo(TaskStatus.RUNNING);
    }

    @Test
    void updateNegativeTestByBlankStatus() throws Exception {
        TaskDto taskDto = TASK_DTO_1_COMPLETED;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", taskDto.title());
        jsonObject.put("description", taskDto.description());
        jsonObject.put("status", "  ");

        mockMvc.perform(put(URL_TEMPLATE)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Optional<Task> task = taskRepository.findById(TASK_ID);
        Assertions.assertThat(task.isPresent()).isTrue();
        Assertions.assertThat(task.get().getTitle()).isEqualTo(TASK_DTO_1.title());
        Assertions.assertThat(task.get().getDescription()).isEqualTo(TASK_DTO_1.description());
        Assertions.assertThat(task.get().getStatus()).isEqualTo(TaskStatus.RUNNING);
    }

    @Test
    void updateNegativeTestByWrongStatus() throws Exception {
        TaskDto taskDto = TASK_DTO_1_COMPLETED;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", taskDto.title());
        jsonObject.put("description", taskDto.description());
        jsonObject.put("status", "wrong status");

        mockMvc.perform(put(URL_TEMPLATE)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Optional<Task> task = taskRepository.findById(TASK_ID);
        Assertions.assertThat(task.isPresent()).isTrue();
        Assertions.assertThat(task.get().getTitle()).isEqualTo(TASK_DTO_1.title());
        Assertions.assertThat(task.get().getDescription()).isEqualTo(TASK_DTO_1.description());
        Assertions.assertThat(task.get().getStatus()).isEqualTo(TaskStatus.RUNNING);
    }

    @Test
    void updateNegativeTestById() throws Exception {
        TaskDto taskDto = TASK_DTO_1_COMPLETED;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", taskDto.title());
        jsonObject.put("description", taskDto.description());
        jsonObject.put("status", taskDto.status());
        mockMvc.perform(put(String.format("/tasks/%s", WRONG_TASK_ID))
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletePositiveTest() throws Exception {
        mockMvc.perform(delete(URL_TEMPLATE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(TASK_1.getTitle())))
                .andExpect(jsonPath("$.description", is(TASK_1.getDescription())))
                .andExpect(jsonPath("$.status", is(TaskStatus.RUNNING.toString())));

        Optional<Task> task = taskRepository.findById(TASK_ID);
        Assertions.assertThat(task.isEmpty()).isTrue();
    }

    @Test
    void deleteNegativeTest() throws Exception {
        mockMvc.perform(delete(String.format("/tasks/%s", WRONG_TASK_ID)))
                .andExpect(status().isBadRequest());
    }

}
