package ru.t1.test.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.t1.test.dto.TaskDto;
import ru.t1.test.entity.Task;
import ru.t1.test.exception.FieldException;
import ru.t1.test.exception.NotFoundException;
import ru.t1.test.mapper.TaskMapper;
import ru.t1.test.repository.TaskRepository;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static ru.t1.test.util.TestData.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    void getTaskByIdPositiveTest() {
        Task task = createTask();
        TaskDto taskDtoActual = new TaskDto(TITLE, DESCRIPTION, STATUS);
        TaskDto taskDtoExpected = new TaskDto(TITLE, DESCRIPTION, STATUS);
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
        when(taskMapper.entityToDto(task)).thenReturn(taskDtoActual);
        Assertions.assertEquals(taskDtoExpected, taskService.getTaskById(1));
    }

    @Test
    void getTaskByIdNegativeTest() {
        when(taskRepository.findById(WRONG_TASK_ID)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> taskService.getTaskById(WRONG_TASK_ID));
    }

    @Test
    void getAllTasksTest() {
        List<Task> tasks = createTasks();
        List<TaskDto> taskDtos = createTaskDtos(tasks);
        when(taskRepository.findAll()).thenReturn(tasks);
        when(taskMapper.entityToDto(tasks.get(0))).thenReturn(taskDtos.get(0));
        Assertions.assertEquals(taskDtos.size(), tasks.size());
    }

    @Test
    void createTaskPositiveTest() {
        Task task = createTask();
        TaskDto taskDto = createTaskDto(task);
        when(taskRepository.save(any())).thenReturn(task);
        int actualId = taskService.createTask(taskDto);
        Assertions.assertEquals(actualId, TASK_ID);
    }

    @Test
    void updateTask() {
        Task task = createTask();
        task.setStatus(COMPLETED_STATUS);
        TaskDto taskDto = createTaskDto(task);
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
        when(taskMapper.dtoToEntity(any())).thenReturn(task);
        when(taskRepository.save(any())).thenReturn(task);
        when(taskMapper.entityToDto(any())).thenReturn(taskDto);
        TaskDto taskDtoActual = taskService.updateTask(task.getId(), taskDto);
        Assertions.assertEquals(taskDtoActual, taskDto);
    }

    @Test
    void updateTaskNegativeTestById() {
        Task task = createTask();
        TaskDto taskDto = createTaskDto(task);
        when(taskRepository.findById(any())).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> taskService.updateTask(TASK_ID, taskDto));
    }

    @Test
    void deleteByIdNegativeTestById() {
        Task task = createTask();
        TaskDto taskDto = createTaskDto(task);
        when(taskRepository.findById(any())).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> taskService.updateTask(TASK_ID, taskDto));
    }

    @Test
    void deleteByIdPositiveTest() {
        Task task = createTask();
        TaskDto taskDto = createTaskDto(task);
        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
        when(taskMapper.entityToDto(task)).thenReturn(taskDto);
        TaskDto taskDtoActual = taskService.deleteById(TASK_ID);
        Assertions.assertEquals(taskDto, taskDtoActual);
    }

    @Test
    void checkTaskDtoPositiveTest() {
        Task task = createTask();
        TaskDto taskDto = createTaskDto(task);
        taskService.checkTaskDto(taskDto);
    }

    @Test
    void checkTaskDtoNegativeTestByEmptyDescriptionField() {
        Task task = createTask();
        TaskDto taskDto = createTaskDtoWithEmptyDescription(task);
        Assertions.assertThrows(FieldException.class, () -> taskService.checkTaskDto(taskDto));
    }

    @Test
    void checkTaskDtoNegativeTestByBlankDescriptionField() {
        Task task = createTask();
        TaskDto taskDto = createTaskDtoWithBlankDescription(task);
        Assertions.assertThrows(FieldException.class, () -> taskService.checkTaskDto(taskDto));
    }

    @Test
    void checkTaskDtoNegativeTestByEmptyTitleField() {
        Task task = createTask();
        TaskDto taskDto = createTaskDtoWithEmptyTitle(task);
        Assertions.assertThrows(FieldException.class, () -> taskService.checkTaskDto(taskDto));
    }

    @Test
    void checkTaskDtoNegativeTestByBlankTitleField() {
        Task task = createTask();
        TaskDto taskDto = createTaskDtoWithBlankTitle(task);
        Assertions.assertThrows(FieldException.class, () -> taskService.checkTaskDto(taskDto));
    }

    @Test
    void checkTaskDtoNegativeTestByWrongStatusField() {
        Task task = createTask();
        TaskDto taskDto = createTaskDtoWithWrongStatus(task);
        Assertions.assertThrows(FieldException.class, () -> taskService.checkTaskDto(taskDto));
    }
}