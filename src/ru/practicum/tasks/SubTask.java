package ru.practicum.tasks;

public class SubTask extends Task{

    Integer epicId;

    SubTask(String taskName, String description, int epicId) {
        super(taskName, description);
        this.epicId = epicId;
    }

    public void setId(Integer id) {
        this.id=id;
    }

    public void setStatus(TaskStatus status){
        this.status = status;
    }

    public void setEpicId(Integer epicId){
        this.epicId = epicId;
    }


    @Override
    public String toString() {
        return "SubTask{id='"+id+"', taskName='"+taskName+"', description='"+description+"', epicId='"+epicId+"', status='"+status+"'}";
    }
}
