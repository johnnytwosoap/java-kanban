package ru.practicum.tasks.service;

import ru.practicum.tasks.model.Epic;
import ru.practicum.tasks.model.SubTask;
import ru.practicum.tasks.model.Task;
import ru.practicum.tasks.model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    Managers managers = new Managers();
    HistoryManager inMemoryHistoryManager = managers.getDefaultHistory();
    Boolean created;
    public static int id = 0;

    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, SubTask> subtasks = new HashMap<>();


    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    @Override
    public Task createTask(Task task) {
        id += 1;
        task.setId(id);
        tasks.put(id, task);
        return task;
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void deleteAllTasks() {
        for (Integer taskId : tasks.keySet()) {
            inMemoryHistoryManager.remove(taskId);
        }
        tasks.clear();
    }

    @Override
    public void deleteTask(int id) {
        inMemoryHistoryManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public Task getTask(int id) {
        inMemoryHistoryManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        if (epics.containsKey(subTask.getEpicId())) {
            id += 1;
            subTask.setId(id);
            subtasks.put(id, subTask);
            epics.get(subTask.getEpicId()).addSubTask(subTask);
            checkEpicStatus(subTask.getEpicId());
        }
        return subTask;
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subtasks.containsKey(subTask.getId()) && subtasks.get(subTask.getId()).getEpicId().equals(subTask.getEpicId())) {
            subtasks.put(subTask.getId(), subTask);
            checkEpicStatus(subTask.getEpicId());
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
            countStatuses.put(subtasks.get(subTaskId).getStatus(), (countStatuses.get(subtasks.get(subTaskId).getStatus()) + 1));
        }
        if (countStatuses.get(TaskStatus.DONE) == epic.getSubTasks().size()) {
            epic.setStatus(TaskStatus.DONE);
        } else if (countStatuses.get(TaskStatus.NEW) == epic.getSubTasks().size()) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }


    @Override
    public void deleteSubTask(int id) {
        if (subtasks.containsKey(id)) {
            epics.get(subtasks.get(id).getEpicId()).removeSubTask(id);
            checkEpicStatus(subtasks.get(id).getEpicId());
        }
        inMemoryHistoryManager.remove(id);
        subtasks.remove(id);
    }

    @Override
    public SubTask getSubTask(int id) {
        inMemoryHistoryManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<SubTask> getAllSubTasksByEpic(Integer epicId) {
        List<SubTask> taskList = new ArrayList<>();
        for (Integer subTaskId: epics.get(epicId).getSubTasks()) {
            taskList.add(subtasks.get(subTaskId));
        }
        return taskList;
    }

    @Override
    public void deleteAllSubTasks() {
        for (Epic epic: epics.values()) {
            epic.removeSubTasks();
            checkEpicStatus(epic.getId());
        }
        for (Integer subTaskId : subtasks.keySet()) {
            inMemoryHistoryManager.remove(subTaskId);
        }
        subtasks.clear();
    }

    @Override
    public Epic createEpic(Epic epic) {
        id += 1;
        epic.setId(id);
        epics.put(id, epic);
        return epic;
    }

    @Override
    public void updateEpic(Epic task) {
        if (epics.containsKey(task.getId())) {
            Epic epic = epics.get(task.getId());
            epic.setDescription(task.getDescription());
            epic.setTaskName(task.getTaskName());
        }
    }


    @Override
    public void deleteEpic(int id) {
        if (!epics.get(id).getSubTasks().isEmpty()) {
            for (Integer subTaskId: epics.get(id).getSubTasks()) {
                inMemoryHistoryManager.remove(subTaskId);
                subtasks.remove(subTaskId);
            }
        }
        inMemoryHistoryManager.remove(id);
        epics.remove(id);
    }

    @Override
    public Epic getEpic(int id) {
        inMemoryHistoryManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public List<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        for (Integer subTaskId : subtasks.keySet()) {
            inMemoryHistoryManager.remove(subTaskId);
        }
        subtasks.clear();
        for (Integer epicId : epics.keySet()) {
            inMemoryHistoryManager.remove(epicId);
        }
        epics.clear();
    }

    @Override
    public Boolean getCreated() {
        return created;
    }

    protected void reloadTask(Task task, int taskId) {
        id = taskId;
        task.setId(taskId);
        tasks.put(taskId, task);
    }

    protected void reloadEpic(Epic epic, int epicId, TaskStatus status) {
        id = epicId;
        epic.setId(epicId);
        epic.setStatus(status);
        epics.put(epicId, epic);
    }

    protected void reloadSubTask(SubTask subTask, int subTaskId) {
        if (epics.containsKey(subTask.getEpicId())) {
            id = subTaskId;
            subTask.setId(subTaskId);
            subtasks.put(subTaskId, subTask);
            epics.get(subTask.getEpicId()).addSubTask(subTask);
            checkEpicStatus(subTask.getEpicId());
        }
    }
}
