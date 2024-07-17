package ru.practicum.tasks.model;

import com.google.gson.JsonObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTasks = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String taskName, String description) {
        super(taskName, description, TaskStatus.NEW);
    }
    public Epic(JsonObject jsonObject) {
        super(jsonObject.get("taskName").getAsString(), jsonObject.get("description").getAsString(), TaskStatus.NEW);
    }

    public void addSubTask(SubTask subTask) {
        this.subTasks.add(subTask.id);
    }

    public List<Integer> getSubTasks() {
        return this.subTasks;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
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

    public String toJson() {
        String startTimeFormatted = "null";
        if (startTime != null) {
            startTimeFormatted = startTime.format(formatter);
        }
        if (subTasks.isEmpty()) {
            return "{\"id\":\"" + id + "\", \"taskName\":\"" + taskName + "\", \"description\":\"" + description + "\", \"status\":\"" + status + "\"}";
        } else {
            StringBuilder subTaskList = new StringBuilder();
            for (Integer subTaskId: subTasks) {
                subTaskList.append(", ").append(subTaskId);
            }
            return "{\"id\":\"" + id + "\", \"taskName\":\"" + taskName + "\", \"description\":\"" + description + "\", \"status\":\"" + status + "\", \"startTime\":\"" + startTimeFormatted + "\", \"duration\":\"" + duration.toMinutes() + "\"}"; //', SubTasks{" + subTaskList.substring(2) + "}}";
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
