package ru.practicum.tasks.model;

import com.google.gson.JsonObject;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {

    private Integer epicId;

    public SubTask(String taskName, String description, TaskStatus status, int epicId, LocalDateTime startTime, int duration) {
        super(taskName, description, status, startTime, duration);
        this.epicId = epicId;
    }
    public SubTask(JsonObject jsonObject) {
        super(jsonObject.get("taskName").getAsString(),
                jsonObject.get("description").getAsString(),
                TaskStatus.valueOf(jsonObject.get("status").getAsString()),
                null,
                jsonObject.get("duration").getAsInt());
        if (!jsonObject.get("startTime").getAsString().equals("null")) {
            this.startTime =LocalDateTime.parse(jsonObject.get("startTime").getAsString(), formatter);
        }

        this.epicId = jsonObject.get("epicId").getAsInt();
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

    public String toJson() {
        String startTimeFormatted = "null";
        if (startTime != null) {
            startTimeFormatted = startTime.format(formatter);
        }
        return "{\"id\":\"" + id + "\", \"taskName\":\"" + taskName + "\", \"description\":\"" + description + "\", \"epicId\":\"" + epicId + "\", \"status\":\"" + status + "\", \"startTime\":\"" + startTimeFormatted + "\", \"duration\":\"" + duration.toMinutes() + "\"}";
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
