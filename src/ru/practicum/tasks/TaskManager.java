package ru.practicum.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    public static int id=0;

    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, SubTask> subtasks = new HashMap<>();

    public Task createTask(Task task){
        id+=1;
        task.setId(id);
        tasks.put(id, task);
        return task;
    }

    public void updateTask(Task task){
        tasks.put(task.id, task);
    }

    public void deleteAllTasks(){
        tasks.clear();
    }

    public void deleteTask(int id){
        tasks.remove(id);
    }

    public Task getTask(int id){
        return tasks.get(id);
    }

    public List<Task> getAllTasks(){
        List<Task> taskList = new ArrayList<>();
        taskList.addAll(tasks.values());
        return taskList;
    }

    public SubTask createSubTask(SubTask subTask){
        id+=1;
        subTask.setId(id);
        subtasks.put(id, subTask);
        if (subTask.epicId != 0) {
            getEpic(subTask.epicId).addSubTask(subTask);
            if (subTask.status!=TaskStatus.NEW){
                checkEpicStatus(subTask.epicId);
            }
        }
        return subTask;
    }

    public void updateSubTask(SubTask subTask){
        if (subTask.epicId != 0) {
            if (subTask.status != TaskStatus.NEW) {
                checkEpicStatus(subTask.epicId);
            }
        }
        subtasks.put(subTask.id, subTask);
    }

    public void checkEpicStatus(Integer epicId) {
        Epic epic = getEpic(epicId);
        HashMap<TaskStatus, Integer> countStatuses = new HashMap<>();
        countStatuses.put(TaskStatus.NEW,0);
        countStatuses.put(TaskStatus.IN_PROGRESS,0);
        countStatuses.put(TaskStatus.DONE,0);
        for (SubTask subtask: epic.subTasks) {
            countStatuses.put(subtask.status, (countStatuses.get(subtask.status)+1));
        }
        if (countStatuses.get(TaskStatus.DONE) == epic.subTasks.size()){
            epic.setStatus(TaskStatus.DONE);
        } else if (countStatuses.get(TaskStatus.NEW) != epic.subTasks.size()){
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
        updateEpic(epic);
    }


    public void deleteSubTask(int id){
        if (subtasks.get(id).epicId != 0) {
            Epic epic = getEpic(subtasks.get(id).epicId);
            epic.removeSubTask(getSubTask(id));
            updateEpic(epic);
        }
        subtasks.remove(id);
    }

    public SubTask getSubTask(int id){
        return subtasks.get(id);
    }

    public List<SubTask> getAllSubTasks(){
        List<SubTask> taskList = new ArrayList<>();
        taskList.addAll(subtasks.values());
        return taskList;
    }

    public List<SubTask> getAllSubTasksByEpic(Integer epicId){
        List<SubTask> taskList = new ArrayList<>();
        for (SubTask subTask: subtasks.values()) {
            if (subTask.epicId == epicId) {
                taskList.add(subTask);
            }
        }
        return taskList;
    }

    public void deleteAllSubTasks(){
        List<SubTask> subTaskList = getAllSubTasks();
        for (SubTask subTask: subTaskList) {
            if (subTask.epicId != 0) {
                Epic epic = getEpic(subTask.epicId);
                epic.removeSubTask(subTask);
                updateEpic(epic);
            }
        }
        subtasks.clear();
    }

    public Epic createEpic(Epic epic){
        id+=1;
        if (!epic.subTasks.isEmpty()) {
            for (SubTask subTask : epic.subTasks) {
                subTask.setEpicId(id);
                updateSubTask(subTask);
            }
        }
        epic.setId(id);
        epics.put(id, epic);
        return epic;

    }

    public void updateEpic(Epic task){
        epics.put(task.id, task);
    }


    public void deleteEpic(int id){
        if (!epics.get(id).subTasks.isEmpty()) {
            for (SubTask subTask: epics.get(id).subTasks) {
                subTask.setEpicId(0);
                updateSubTask(subTask);
            }
        }
        epics.remove(id);
    }

    public Epic getEpic(int id){
        return epics.get(id);
    }

    public List<Epic> getAllEpic(){
        List<Epic> taskList = new ArrayList<>();
        taskList.addAll(epics.values());
        return taskList;
    }

    public void deleteAllEpics(){
        List<Epic> epicList = getAllEpic();
        for (Epic epic: epicList) {
            if (!epic.subTasks.isEmpty()) {
                for (SubTask subTask : epic.subTasks) {
                    subTask.setEpicId(0);
                    updateSubTask(subTask);
                }
            }
        }
        epics.clear();
    }

}
