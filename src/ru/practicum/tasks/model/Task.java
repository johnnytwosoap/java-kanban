package ru.practicum.tasks.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    protected Integer id;
    protected String taskName;
    protected String description;
    protected TaskStatus status;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(String taskName, String description, TaskStatus status, LocalDateTime startTime, int duration) {
        this.taskName = taskName;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(duration);
    }

    public Task(String taskName, String description, TaskStatus status) {
        this.taskName = taskName;
        this.description = description;
        this.status = status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskStatus getStatus() {
        return this.status;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null) {
            return null;
        }
        return this.startTime.plus(this.duration);
    }

    public String toString() {
        String startTimeFormatted = "null";
        if (startTime != null) {
            startTimeFormatted = startTime.format(formatter);
        }
        return "Task{id='" + id + "', taskName='" + taskName + "', description='" + description + "', status='" + status + "', startTime='" + startTimeFormatted + "', duration='" + duration.toMinutes() + "'}";
    }

    public String toFile() {
        String startTimeFormatted = "null";
        String durationToMinutes = "null";
        if (startTime != null) {
            startTimeFormatted = startTime.format(formatter);
        }
        if (duration != null) {
            durationToMinutes = String.valueOf(duration.toMinutes());
        }
        return id + ",Task," + taskName + "," + description + "," + status + "," + startTimeFormatted + "," + durationToMinutes;
    }
}
