package ru.practicum.tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task{
    List<SubTask> subTasks = new ArrayList<>();

    Epic(String taskName, String description, List<SubTask> subTasksId) {
        super(taskName, description);
        this.subTasks.addAll(subTasksId);
    }

    public void setId(Integer id) {
        this.id=id;
    }

    public void setStatus(TaskStatus status){
        this.status = status;
    }

    public int addSubTask(SubTask subTask) {
        this.subTasks.add(subTask);
        return id;
    }

    public void removeSubTask(SubTask subTaskId) {
        this.subTasks.remove(subTaskId);
    }

    @Override
    public String toString() {
        if (subTasks.isEmpty()) {
            return "Epic{id='" + id + "', taskName='" + taskName + "', description='" + description +"', status='"+status+"'}";
        } else {
            String subTaskList = "";
            for (SubTask subTask: subTasks) {
                subTaskList = subTaskList +", "+ subTask;
            }
            return "Epic{id='" + id + "', taskName='" + taskName + "', description='" + description +"', status='"+status+"', SubTasks{" + subTaskList.substring(2) + "}}";
        }
    }

}
