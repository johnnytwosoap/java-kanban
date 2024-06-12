package ru.practicum.tasks.model;

public class Task {

    protected Integer id;
    protected String taskName;
    protected String description;
    protected TaskStatus status;

    public Task(String taskName, String description, TaskStatus status) {
        this.taskName = taskName;
        this.description = description;
        this.status = status;
    }

    public void setId(Integer id) {
        this.id=id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setTaskName(String taskName) {
        this.taskName=taskName;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public void setDescription(String description) {
        this.description=description;
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

    public String toString() {
        return "Task{id='" + id + "', taskName='" + taskName + "', description='" + description + "', status='" + status + "'}";
    }
}
