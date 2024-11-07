package ru.t1.test.entity;

import jakarta.persistence.*;
import ru.t1.test.TaskStatus;

@Entity
@Table(name = "tasks")
@SequenceGenerator(name = "task_seq", sequenceName = "task_sequence", allocationSize = 1)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq")
    private int id;

    private String title;

    private String description;

    private TaskStatus status;

    public Task() {
    }

    public Task(String title, String description, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = TaskStatus.valueOf(status);
    }
}
