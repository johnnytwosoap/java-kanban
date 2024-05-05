package ru.practicum.tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task{
    private List<Integer> subTasks = new ArrayList<>();

    public Epic(String taskName, String description) {
        super(taskName, description, TaskStatus.NEW);
    }

    public void addSubTask(SubTask subTask) {
        this.subTasks.add(subTask.id);
    }

    public List<Integer> getSubTasks() {
        return this.subTasks;
    }


    public void removeSubTasks() {
        this.subTasks.clear();
    }

    public void removeSubTask(Integer subTaskId) {
        this.subTasks.remove(subTaskId);
    }

    @Override
    public String toString() {
        if (subTasks.isEmpty()) {
            return "Epic{id='" + id + "', taskName='" + taskName + "', description='" + description +"', status='"+status+"'}";
        } else {
            String subTaskList = "";
            for (Integer subTaskId: subTasks) {
                subTaskList = subTaskList +", "+ subTaskId;
            }
            return "Epic{id='" + id + "', taskName='" + taskName + "', description='" + description +"', status='"+status+"', SubTasks{" + subTaskList.substring(2) + "}}";
        }
    }

}
