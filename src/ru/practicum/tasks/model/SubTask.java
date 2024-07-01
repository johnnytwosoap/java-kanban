package ru.practicum.tasks.model;

import java.time.LocalDateTime;

public class SubTask extends Task {

    private Integer epicId;

    public SubTask(String taskName, String description, TaskStatus status, int epicId, LocalDateTime startTime, int duration) {
        super(taskName, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public SubTask(String taskName, String description, TaskStatus status, int epicId) {
        super(taskName, description, status);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return this.epicId;
    }

    @Override
    public String toString() {
        String startTimeFormatted = "null";
        if (startTime != null) {
            startTimeFormatted = startTime.format(formatter);
        }
        return "SubTask{id='" + id + "', taskName='" + taskName + "', description='" + description + "', epicId='" + epicId + "', status='" + status + "', startTime='" + startTimeFormatted + "', duration='" + duration.toMinutes() + "'}";
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
        return id + ",SubTask," + taskName + "," + description + "," + status + "," + startTimeFormatted + "," + durationToMinutes + "," + epicId;
    }
}
