package ru.practicum.tasks.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTasks = new ArrayList<>();

    private LocalDateTime startTime;
    private Duration duration;
    private LocalDateTime endTime;

    public Epic(String taskName, String description) {
        super(taskName, description, TaskStatus.NEW);
    }

    public void addSubTask(SubTask subTask) {
        this.subTasks.add(subTask.id);
    }

    public List<Integer> getSubTasks() {
        return this.subTasks;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public void removeSubTasks() {
        this.subTasks.clear();
    }

    public void removeSubTask(Integer subTaskId) {
        this.subTasks.remove(subTaskId);
    }

    @Override
    public String toString() {
        String startTimeFormatted = "null";
        if (startTime != null) {
            startTimeFormatted = startTime.format(formatter);
        }
        if (subTasks.isEmpty()) {
            return "Epic{id='" + id + "', taskName='" + taskName + "', description='" + description + "', status='" + status + "'}";
        } else {
            StringBuilder subTaskList = new StringBuilder();
            for (Integer subTaskId: subTasks) {
                subTaskList.append(", ").append(subTaskId);
            }
            return "Epic{id='" + id + "', taskName='" + taskName + "', description='" + description + "', status='" + status + "', startTime='" + startTimeFormatted + "', duration='" + duration.toMinutes() + "', SubTasks{" + subTaskList.substring(2) + "}}";
        }
    }


    @Override
    public String toFile() {
        String startTimeFormatted = "null";
        String durationToMinutes = "null";
        if (startTime != null) {
            startTimeFormatted = startTime.format(formatter);
        }
        if (duration != null) {
            durationToMinutes = String.valueOf(duration.toMinutes());
        }
        return id + ",Epic," + taskName + "," + description + "," + status + "," + startTimeFormatted + "," + durationToMinutes;
    }

}
