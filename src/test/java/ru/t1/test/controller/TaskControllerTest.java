package ru.t1.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.t1.test.dto.TaskDto;
import ru.t1.test.entity.Task;
import ru.t1.test.service.TaskService;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.t1.test.util.TestData.*;

@WebMvcTest(controllers = {TaskController.class})
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @Test
    void get() throws Exception {
        Task task = createTask();
        TaskDto taskDto = createTaskDto(task);
        when(taskService.getTaskById(TASK_ID)).thenReturn(taskDto);
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAll() throws Exception {
        List<Task> tasks = createTasks();
        List<TaskDto> taskDtos = createTaskDtos(tasks);
        when(taskService.getAllTasks()).thenReturn(taskDtos);
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE_TASKS))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void create() throws Exception {
        Task task = createTask();
        TaskDto taskDto = createTaskDto(task);
        when(taskService.createTask(taskDto)).thenReturn(task.getId());
        mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE_TASKS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void update() throws Exception {
        Task task = createTask();
        TaskDto taskDto = createTaskDto(task);
        when(taskService.updateTask(task.getId(), taskDto)).thenReturn(taskDto);
        mockMvc.perform(MockMvcRequestBuilders.put(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void delete() throws Exception {
        Task task = createTask();
        TaskDto taskDto = createTaskDto(task);
        when(taskService.deleteById(task.getId())).thenReturn(taskDto);
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE))
                .andDo(print())
                .andExpect(status().isOk());
    }
}