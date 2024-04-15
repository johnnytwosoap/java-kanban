package ru.practicum.tasks;

public class SubTask extends Task{

    private Integer epicId;

    SubTask(String taskName, String description, int epicId) {
        super(taskName, description);
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
