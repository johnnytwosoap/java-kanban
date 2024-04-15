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
        task.setStatus(TaskStatus.NEW);
        tasks.put(id, task);
        return task;
    }

    public void updateTask(Task task){
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.id, task);
        }
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
        if (epics.containsKey(subTask.getEpicId())) {
            id+=1;
            subTask.setId(id);
            subTask.setStatus(TaskStatus.NEW);
            subtasks.put(id, subTask);
            epics.get(subTask.getEpicId()).addSubTask(subTask);
            checkEpicStatus(subTask.getEpicId());
        }
        return subTask;
    }

    public void updateSubTask(SubTask subTask){
        if (subtasks.containsKey(subTask.getId()) && subtasks.get(subTask.getId()).getEpicId()== subTask.getEpicId()) {
            if (subTask.status != TaskStatus.NEW) {
                checkEpicStatus(subTask.getEpicId());
            }
            subtasks.put(subTask.id, subTask);
        }
    }

    private void checkEpicStatus(Integer epicId) {
        Epic epic = epics.get(epicId);
        if (epic.getSubTasks().isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }
        HashMap<TaskStatus, Integer> countStatuses = new HashMap<>();
        countStatuses.put(TaskStatus.NEW,0);
        countStatuses.put(TaskStatus.IN_PROGRESS,0);
        countStatuses.put(TaskStatus.DONE,0);
        for (Integer subTaskId: epic.getSubTasks()) {
            countStatuses.put(subtasks.get(subTaskId).getStatus(), (countStatuses.get(subtasks.get(subTaskId).getStatus())+1));
        }
         if (countStatuses.get(TaskStatus.DONE) == epic.getSubTasks().size()){
            epic.setStatus(TaskStatus.DONE);
        } else if (countStatuses.get(TaskStatus.NEW) != epic.getSubTasks().size()){
            epic.setStatus(TaskStatus.IN_PROGRESS);
        } else if (countStatuses.get(TaskStatus.NEW) == epic.getSubTasks().size()){
            epic.setStatus(TaskStatus.NEW);
        } else {
            epic.setStatus(TaskStatus.NEW);
        }
    }


    public void deleteSubTask(int id){
        if (subtasks.containsKey(id)) {
            epics.get(subtasks.get(id).getEpicId()).removeSubTask(id);
            checkEpicStatus(subtasks.get(id).getEpicId());
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
        for (Integer subTaskId: epics.get(epicId).getSubTasks()) {
            taskList.add(subtasks.get(subTaskId));
        }
        return taskList;
    }

    public void deleteAllSubTasks(){
        for (Epic epic: epics.values()){
            epic.removeSubTasks();
            checkEpicStatus(epic.getId());
        }
        subtasks.clear();
    }

    public Epic createEpic(Epic epic){
        id+=1;
        epic.setId(id);
        epics.put(id, epic);
        checkEpicStatus(id);
        return epic;

    }

    public void updateEpic(Epic task){
        if (epics.containsKey(task.getId())) {
            Epic epic = epics.get(task.getId());
            epic.setDescription(task.getDescription());
            epic.setTaskName(task.getTaskName());
        }
    }


    public void deleteEpic(int id){
        if (!epics.get(id).getSubTasks().isEmpty()) {
            for (Integer subTaskId: epics.get(id).getSubTasks()) {
                subtasks.remove(subTaskId);
            }
            epics.get(id).removeSubTasks();
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
        subtasks.clear();
        epics.clear();
    }

}
