package ru.t1.test.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.t1.test.PostgresSQLTestContainerExtension;
import ru.t1.test.TaskStatus;
import ru.t1.test.repository.TaskRepository;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.t1.test.util.TestData.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TaskIntegrationTest extends PostgresSQLTestContainerExtension {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        taskRepository.saveAll(List.of(TASK_1, TASK_2, TASK_3, TASK_4));
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



}
