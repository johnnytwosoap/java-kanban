package ru.practicum.tasks;

public class SubTask extends Task{

    private Integer epicId;

    SubTask(String taskName, String description, TaskStatus status, int epicId) {
        super(taskName, description, status);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return this.epicId;
    }

    @Override
    public String toString() {
        return "SubTask{id='"+id+"', taskName='"+taskName+"', description='"+description+"', epicId='"+epicId+"', status='"+status+"'}";
    }
}
