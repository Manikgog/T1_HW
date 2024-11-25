package ru.t1.test.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.t1.test.dto.TaskDto;
import ru.t1.test.service.TaskService;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> get(@PathVariable(name = "id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getTaskById(id));
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllTasks());
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody TaskDto task) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> update(@PathVariable(name = "id") int id, @RequestBody TaskDto task) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.updateTask(id, task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskDto> delete(@PathVariable(name = "id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.deleteById(id));
    }

}
