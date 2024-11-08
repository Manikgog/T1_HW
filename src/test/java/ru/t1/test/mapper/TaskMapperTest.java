package ru.t1.test.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.t1.test.TaskStatus;
import ru.t1.test.dto.TaskDto;
import ru.t1.test.entity.Task;
import ru.t1.test.exception.IllegalStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskMapperTest {

    private final TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);

    @Test
    void dtoToEntityPositiveTest() {
        TaskDto taskDto = new TaskDto("title test", "description test", "running");
        Task task = taskMapper.dtoToEntity(taskDto);
        assertEquals(taskDto.title(), task.getTitle());
        assertEquals(taskDto.description(), task.getDescription());
        assertEquals(taskDto.status(), task.getStatus().getStatus());
    }

    @Test
    void dtoToEntityNegativeTestByStatus() {
        TaskDto taskDto = new TaskDto("title test", "description test", "incorrect status");
        IllegalStatusException exception = Assertions.assertThrows(IllegalStatusException.class, () -> taskMapper.dtoToEntity(taskDto));
        Assertions.assertEquals("Unknown status: " + taskDto.status(), exception.getMessage());
    }

    @Test
    void entityToDtoPositiveTest() {
        Task task = new Task("title test", "description test", TaskStatus.RUNNING);
        task.setId(1);
        TaskDto taskDto = taskMapper.entityToDto(task);
        assertEquals(task.getTitle(), taskDto.title());
        assertEquals(task.getDescription(), taskDto.description());
        assertEquals(task.getStatus().toString(), taskDto.status());
    }

    @Test
    void stringToTaskStatusPositiveTest() {
        String taskStatusString = "running";
        TaskStatus taskStatus = taskMapper.stringToTaskStatus(taskStatusString);
        Assertions.assertEquals(taskStatusString, taskStatus.getStatus());
    }

    @Test
    void taskStatusToStringPositiveTest() {
        TaskStatus taskStatus = TaskStatus.RUNNING;
        String taskStatusString = taskMapper.taskStatusToString(taskStatus);
        Assertions.assertEquals(taskStatusString, taskStatus.getStatus());
    }
}