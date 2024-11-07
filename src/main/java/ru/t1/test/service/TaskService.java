package ru.t1.test.service;

import org.springframework.stereotype.Service;
import ru.t1.test.dto.TaskDto;
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

    public TaskDto createTask(TaskDto taskDto) {
        return taskMapper.entityToDto(taskRepository.save(taskMapper.dtoToEntity(taskDto)));
    }

    public TaskDto updateTask(int id, TaskDto taskDto) {
        return taskMapper.entityToDto(taskRepository.save(taskMapper.dtoToEntity(taskDto)));
    }

}
