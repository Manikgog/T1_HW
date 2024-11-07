package ru.t1.test;

import ru.t1.test.exception.IllegalStatusException;

public enum TaskStatus {
    RUNNING("running"),
    COMPLETED("completed");

    private final String status;

    TaskStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static TaskStatus fromString(String status) {
        for (TaskStatus taskStatus : TaskStatus.values()) {
            if (taskStatus.status.equalsIgnoreCase(status)) {
                return taskStatus;
            }
        }
        throw new IllegalStatusException("Unknown status: " + status);
    }

    @Override
    public String toString() {
        return status;
    }
}
