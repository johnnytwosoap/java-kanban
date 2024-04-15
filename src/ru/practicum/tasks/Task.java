package ru.practicum.tasks;

public class Task {

    protected Integer id;
    String taskName;
    String description;
    TaskStatus status;

    Task(String taskName, String description) {
        this.taskName = taskName;
        this.description = description;
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

    public void setStatus(TaskStatus status){
        this.status = status;
    }

    public TaskStatus getStatus() {
        return this.status;
    }

    public String toString() {
        return "Task{id='"+id+"', taskName='"+taskName+"', description='"+description+"', status='"+status+"'}";
    }
}
