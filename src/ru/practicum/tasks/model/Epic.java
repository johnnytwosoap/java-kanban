package ru.practicum.tasks.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTasks = new ArrayList<>();

    public Epic(String taskName, String description) {
        super(taskName, description, TaskStatus.NEW, null, 0);
    }

    public void addSubTask(SubTask subTask) {
        this.subTasks.add(subTask.id);
    }

    public List<Integer> getSubTasks() {
        return this.subTasks;
    }


    public void removeSubTasks() {
        this.subTasks.clear();
    }

    public void removeSubTask(Integer subTaskId) {
        this.subTasks.remove(subTaskId);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
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
        if (startTime != null) {
            startTimeFormatted = startTime.format(formatter);
        }
        return id + ",Epic," + taskName + "," + description + "," + status + "," + startTimeFormatted + "," + duration.toMinutes();
    }

}
