package ru.practicum.tasks.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
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
            return "Epic{id='" + id + "', taskName='" + taskName + "', description='" + description + "', status='" + status + "'}";
        } else {
            StringBuilder subTaskList = new StringBuilder();
            for (Integer subTaskId: subTasks) {
                subTaskList.append(", ").append(subTaskId);
            }
            return "Epic{id='" + id + "', taskName='" + taskName + "', description='" + description + "', status='" + status + "', SubTasks{" + subTaskList.substring(2) + "}}";
        }
    }


    @Override
    public String toFile() {
        if (subTasks.isEmpty()) {
            return id + ",Epic," + taskName + "," + description + "," + status;
        } else {
            StringBuilder subTaskList = new StringBuilder();
            for (Integer subTaskId: subTasks) {
                subTaskList.append(", ").append(subTaskId);
            }
            return id + ",Epic," + taskName + "," + description + "," + status +",{" + subTaskList.substring(2) + "}";
        }

    }

}
