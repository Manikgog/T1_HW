package ru.t1.test.util;

import ru.t1.test.dto.TaskDto;
import ru.t1.test.entity.Task;
import java.util.ArrayList;
import java.util.List;

public class TestData {
    public static int TASK_ID = 1;
    public static int WRONG_TASK_ID = -1;
    public static String TITLE = "test title";
    public static String DESCRIPTION = "test description";
    public static String STATUS = "RUNNING";
    public static String COMPLETED_STATUS = "COMPLETED";
    public static String URL_TEMPLATE = String.format("/tasks/%s", TASK_ID);
    public static String URL_TEMPLATE_TASKS = "/tasks";

    public static List<Task> createTasks() {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            Task task = new Task();
            task.setTitle(TITLE);
            task.setDescription(DESCRIPTION);
            task.setStatus(STATUS);
            tasks.add(task);
        }
        return tasks;
    }

    public static List<TaskDto> createTaskDtos(List<Task> tasks) {
        return tasks.stream()
                .map(t -> new TaskDto(t.getTitle(), t.getDescription(), t.getStatus().toString())).toList();
    }

    public static Task createTask() {
        Task task = new Task();
        task.setId(TASK_ID);
        task.setTitle(TITLE);
        task.setDescription(DESCRIPTION);
        task.setStatus(STATUS);
        return task;
    }

    public static TaskDto createTaskDto(Task task) {
        return new TaskDto(task.getTitle(), task.getDescription(), task.getStatus().toString());
    }

    public static TaskDto createTaskDtoWithEmptyDescription(Task task) {
        return new TaskDto(task.getTitle(), "", task.getStatus().toString());
    }

    public static TaskDto createTaskDtoWithBlankDescription(Task task) {
        return new TaskDto(task.getTitle(), "   ", task.getStatus().toString());
    }

    public static TaskDto createTaskDtoWithEmptyTitle(Task task) {
        return new TaskDto("", task.getDescription(), task.getStatus().toString());
    }

    public static TaskDto createTaskDtoWithBlankTitle(Task task) {
        return new TaskDto("   ", "   ", task.getStatus().toString());
    }

    public static TaskDto createTaskDtoWithWrongStatus(Task task) {
        return new TaskDto(task.getTitle(), task.getDescription(), "wrong status");
    }
}
