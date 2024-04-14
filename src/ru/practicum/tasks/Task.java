package ru.practicum.tasks;

public class Task {

    Integer id;
    String taskName;
    String description;
    TaskStatus status = TaskStatus.NEW;

    Task(String taskName, String description) {
        this.taskName = taskName;
        this.description = description;
    }

    public void setId(Integer id) {
        this.id=id;
    }

    public void setStatus(TaskStatus status){
        this.status = status;
    }

    public String toString() {
        return "Task{id='"+id+"', taskName='"+taskName+"', description='"+description+"', status='"+status+"'}";
    }
}
