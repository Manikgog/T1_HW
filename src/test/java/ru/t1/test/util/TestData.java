package ru.t1.test.util;

import ru.t1.test.TaskStatus;
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
    public static TaskDto TASK_DTO_1 = new TaskDto("test title 1", "test description 1", "running");
    public static TaskDto TASK_DTO_2 = new TaskDto("test title 2", "test description 2", "running");
    public static TaskDto TASK_DTO_3 = new TaskDto("test title 3", "test description 3", "completed");
    public static TaskDto TASK_DTO_4 = new TaskDto("test title 4", "test description 4", "completed");
    public static TaskDto TASK_DTO_5 = new TaskDto("test title 5", "test description 5", "running");
    public static TaskDto TASK_DTO_1_COMPLETED = new TaskDto("test title 1", "test description 1", "completed");
    public static TaskDto TASK_DTO_COMPLETED = new TaskDto("test title 1", "test description 1", "completed");

    public static Task TASK_1 = new Task("test title 1", "test description 1", TaskStatus.RUNNING);
    public static Task TASK_2 = new Task("test title 2", "test description 2", TaskStatus.RUNNING);
    public static Task TASK_3 = new Task("test title 3", "test description 3", TaskStatus.RUNNING);
    public static Task TASK_4 = new Task("test title 4", "test description 4", TaskStatus.RUNNING);

    public static List<TaskDto> TASK_DTO_LIST = List.of(TASK_DTO_1,
            TASK_DTO_2,
            TASK_DTO_3,
            TASK_DTO_4);

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
