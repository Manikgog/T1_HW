package ru.t1.test.service;

import org.springframework.stereotype.Service;
import ru.t1.test.TaskStatus;
import ru.t1.test.dto.TaskDto;
import ru.t1.test.entity.Task;
import ru.t1.test.exception.FieldException;
import ru.t1.test.exception.NotFoundException;
import ru.t1.test.mapper.TaskMapper;
import ru.t1.test.repository.TaskRepository;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public TaskDto getTaskById(int id) {
        return taskMapper.entityToDto(taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found")));
    }

    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream().map(taskMapper::entityToDto).toList();
    }

    public int createTask(TaskDto taskDto) {
        checkTaskDto(taskDto);
        Task task = taskMapper.dtoToEntity(taskDto);
        return taskRepository.save(task).getId();
    }

    public TaskDto updateTask(int id, TaskDto taskDto) {
        checkTaskDto(taskDto);
        taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found"));
        Task task = taskMapper.dtoToEntity(taskDto);
        task.setId(id);
        return taskMapper.entityToDto(taskRepository.save(task));
    }

    public TaskDto deleteById(int id) {
        Task taskToDelete = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found"));
        taskRepository.deleteById(id);
        return taskMapper.entityToDto(taskToDelete);
    }

    public void checkTaskDto(TaskDto taskDto) {
        StringBuilder stringBuilder = new StringBuilder();
        if(taskDto.description().isEmpty() || taskDto.description().isBlank()) {
            stringBuilder.append("Description is empty. ");
        }
        if(taskDto.title().isEmpty() || taskDto.title().isBlank()) {
            stringBuilder.append("Title is empty. ");
        }
        if(taskDto.status().isBlank() || taskDto.status().isEmpty()) {
            stringBuilder.append("Status is empty. ");
        }
        if(!taskDto.status().equalsIgnoreCase(TaskStatus.COMPLETED.getStatus()) &&
        !taskDto.status().equalsIgnoreCase(TaskStatus.RUNNING.getStatus())){
            stringBuilder.append("Status is incorrect.");
        }
        if(!stringBuilder.isEmpty()) {
            throw new FieldException(stringBuilder.toString());
        }
    }
}
