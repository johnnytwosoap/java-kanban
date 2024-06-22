package ru.practicum.tasks.model;

public class SubTask extends Task {

    private Integer epicId;

    public SubTask(String taskName, String description, TaskStatus status, int epicId) {
        super(taskName, description, status);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return this.epicId;
    }

    @Override
    public String toString() {
        return "SubTask{id='" + id + "', taskName='" + taskName + "', description='" + description + "', epicId='" + epicId + "', status='" + status + "'}";
    }

    @Override
    public String toFile() {
        return id + ",SubTask," + taskName + "," + description + "," + status + "," + epicId;
    }
}
