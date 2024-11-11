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
import java.util.Random;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.t1.test.util.TestData.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TaskIntegrationTest extends PostgresSQLTestContainerExtension {

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
        int id = getRandomId();
        Task expectedTask = taskRepository.findById(id).get();
        mockMvc.perform(get("/tasks/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(expectedTask.getTitle())))
                .andExpect(jsonPath("$.description", is(expectedTask.getDescription())))
                .andExpect(jsonPath("$.status", is(expectedTask.getStatus().name().toLowerCase())));
    }

    @Test
    void getNegativeTest() throws Exception {
        mockMvc.perform(get(String.format("/tasks/%s", WRONG_TASK_ID)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postPositiveTest() throws Exception {
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
                .andExpect(content().string(String.valueOf(getMaxId())));

        Optional<Task> task = taskRepository.findById(getMaxId());
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
        int id = getRandomId();
        TaskDto taskDto = TASK_DTO_COMPLETED;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", taskDto.title());
        jsonObject.put("description", taskDto.description());
        jsonObject.put("status", taskDto.status());

        mockMvc.perform(put("/tasks/" + id)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(taskRepository.findById(id).get().getTitle())))
                .andExpect(jsonPath("$.description", is(taskRepository.findById(id).get().getDescription())))
                .andExpect(jsonPath("$.status", is(TaskStatus.COMPLETED.toString())));
    }


    @Test
    void updateNegativeTestByEmptyTitle() throws Exception {
        int id = getRandomId();
        Task taskBefore = taskRepository.findById(id).get();
        TaskDto taskDto = TASK_DTO_COMPLETED;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "");
        jsonObject.put("description", taskDto.description());
        jsonObject.put("status", taskDto.status());

        mockMvc.perform(put("/tasks/" + id)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Optional<Task> taskAfter = taskRepository.findById(id);
        Assertions.assertThat(taskAfter.isPresent()).isTrue();
        Assertions.assertThat(taskAfter.get().getTitle()).isEqualTo(taskBefore.getTitle());
        Assertions.assertThat(taskAfter.get().getDescription()).isEqualTo(taskBefore.getDescription());
        Assertions.assertThat(taskAfter.get().getStatus()).isEqualTo(taskBefore.getStatus());
    }

    @Test
    void updateNegativeTestByBlankTitle() throws Exception {
        int id = getRandomId();
        Task taskBefore = taskRepository.findById(id).get();
        TaskDto taskDto = TASK_DTO_COMPLETED;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "  ");
        jsonObject.put("description", taskDto.description());
        jsonObject.put("status", taskDto.status());

        mockMvc.perform(put("/tasks/" + id)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Optional<Task> taskAfter = taskRepository.findById(id);
        Assertions.assertThat(taskAfter.isPresent()).isTrue();
        Assertions.assertThat(taskAfter.get().getTitle()).isEqualTo(taskBefore.getTitle());
        Assertions.assertThat(taskAfter.get().getDescription()).isEqualTo(taskBefore.getDescription());
        Assertions.assertThat(taskAfter.get().getStatus()).isEqualTo(taskBefore.getStatus());
    }

    @Test
    void updateNegativeTestByEmptyDescription() throws Exception {
        int id = getRandomId();
        Task taskBefore = taskRepository.findById(id).get();
        TaskDto taskDto = TASK_DTO_COMPLETED;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", taskDto.title());
        jsonObject.put("description", "");
        jsonObject.put("status", taskDto.status());

        mockMvc.perform(put("/tasks/" + id)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Optional<Task> taskAfter = taskRepository.findById(id);
        Assertions.assertThat(taskAfter.isPresent()).isTrue();
        Assertions.assertThat(taskAfter.get().getTitle()).isEqualTo(taskBefore.getTitle());
        Assertions.assertThat(taskAfter.get().getDescription()).isEqualTo(taskBefore.getDescription());
        Assertions.assertThat(taskAfter.get().getStatus()).isEqualTo(taskBefore.getStatus());
    }

    @Test
    void updateNegativeTestByBlankDescription() throws Exception {
        int id = getRandomId();
        Task taskBefore = taskRepository.findById(id).get();
        TaskDto taskDto = TASK_DTO_COMPLETED;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", taskDto.title());
        jsonObject.put("description", "  ");
        jsonObject.put("status", taskDto.status());

        mockMvc.perform(put("/tasks/" + id)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Optional<Task> taskAfter = taskRepository.findById(id);
        Assertions.assertThat(taskAfter.isPresent()).isTrue();
        Assertions.assertThat(taskAfter.get().getTitle()).isEqualTo(taskBefore.getTitle());
        Assertions.assertThat(taskAfter.get().getDescription()).isEqualTo(taskBefore.getDescription());
        Assertions.assertThat(taskAfter.get().getStatus()).isEqualTo(taskBefore.getStatus());
    }

    @Test
    void updateNegativeTestByEmptyStatus() throws Exception {
        int id = getRandomId();
        Task taskBefore = taskRepository.findById(id).get();
        TaskDto taskDto = TASK_DTO_COMPLETED;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", taskDto.title());
        jsonObject.put("description", taskDto.description());
        jsonObject.put("status", "");

        mockMvc.perform(put("/tasks/" + id)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Optional<Task> taskAfter = taskRepository.findById(id);
        Assertions.assertThat(taskAfter.isPresent()).isTrue();
        Assertions.assertThat(taskAfter.get().getTitle()).isEqualTo(taskBefore.getTitle());
        Assertions.assertThat(taskAfter.get().getDescription()).isEqualTo(taskBefore.getDescription());
        Assertions.assertThat(taskAfter.get().getStatus()).isEqualTo(taskBefore.getStatus());
    }

    @Test
    void updateNegativeTestByBlankStatus() throws Exception {
        int id = getRandomId();
        Task taskBefore = taskRepository.findById(id).get();
        TaskDto taskDto = TASK_DTO_COMPLETED;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", taskDto.title());
        jsonObject.put("description", taskDto.description());
        jsonObject.put("status", "  ");

        mockMvc.perform(put("/tasks/" + id)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Optional<Task> taskAfter = taskRepository.findById(id);
        Assertions.assertThat(taskAfter.isPresent()).isTrue();
        Assertions.assertThat(taskAfter.get().getTitle()).isEqualTo(taskBefore.getTitle());
        Assertions.assertThat(taskAfter.get().getDescription()).isEqualTo(taskBefore.getDescription());
        Assertions.assertThat(taskAfter.get().getStatus()).isEqualTo(taskBefore.getStatus());
    }

    @Test
    void updateNegativeTestByWrongStatus() throws Exception {
        int id = getRandomId();
        Task taskBefore = taskRepository.findById(id).get();
        TaskDto taskDto = TASK_DTO_COMPLETED;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", taskDto.title());
        jsonObject.put("description", taskDto.description());
        jsonObject.put("status", "wrong status");

        mockMvc.perform(put("/tasks/" + id)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Optional<Task> taskAfter = taskRepository.findById(id);
        Assertions.assertThat(taskAfter.isPresent()).isTrue();
        Assertions.assertThat(taskAfter.get().getTitle()).isEqualTo(taskBefore.getTitle());
        Assertions.assertThat(taskAfter.get().getDescription()).isEqualTo(taskBefore.getDescription());
        Assertions.assertThat(taskAfter.get().getStatus()).isEqualTo(taskBefore.getStatus());
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
        int id = getRandomId();
        Task taskBefore = taskRepository.findById(id).get();
        mockMvc.perform(delete("/tasks/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(taskBefore.getTitle())))
                .andExpect(jsonPath("$.description", is(taskBefore.getDescription())))
                .andExpect(jsonPath("$.status", is(taskBefore.getStatus().name().toLowerCase())));

        Optional<Task> task = taskRepository.findById(id);
        Assertions.assertThat(task.isEmpty()).isTrue();
    }

    @Test
    void deleteNegativeTest() throws Exception {
        mockMvc.perform(delete(String.format("/tasks/%s", WRONG_TASK_ID)))
                .andExpect(status().isBadRequest());
    }

    private int getMaxId(){
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().mapToInt(t -> t.getId()).max().getAsInt();
    }


    private int getRandomId() {
        List<Task> tasks = taskRepository.findAll();
        Random rnd = new Random();
        int index = rnd.nextInt(tasks.size());
        int id = tasks.get(index).getId();
        return id;
    }

}
