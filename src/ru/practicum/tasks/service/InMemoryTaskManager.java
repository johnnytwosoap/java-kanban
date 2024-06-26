package ru.practicum.tasks.service;

import ru.practicum.tasks.model.Epic;
import ru.practicum.tasks.model.SubTask;
import ru.practicum.tasks.model.Task;
import ru.practicum.tasks.model.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    Managers managers = new Managers();
    HistoryManager inMemoryHistoryManager = managers.getDefaultHistory();
    Boolean created;
    public static int id = 0;

    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, SubTask> subtasks = new HashMap<>();

    private Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }


    @Override
    public Task createTask(Task task) {
        if (!checkTime(task)) {
            throw new NotValidTaskException(task.toString());
        }
        id += 1;
        task.setId(id);
        tasks.put(id, task);
        setPrioritizedTasks(task);
        return task;
    }

    @Override
    public Task updateTask(Task task) {
        if (!checkTime(task)) {
            throw new NotValidTaskException(task.toString());
        }
        if (tasks.containsKey(task.getId())) {
            removePrioritizedTasks(task);
            tasks.put(task.getId(), task);
            setPrioritizedTasks(task);
        }
        return task;
    }

    @Override
    public void deleteAllTasks() {
        tasks.values().forEach(this::removePrioritizedTasks);
        tasks.keySet().forEach(inMemoryHistoryManager::remove);
        tasks.clear();
    }

    @Override
    public void deleteTask(int id) {
        removePrioritizedTasks(tasks.get(id));
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
        if (!checkTime(subTask)) {
            throw new NotValidTaskException(subTask.toString());
        }
        if (epics.containsKey(subTask.getEpicId())) {
            id += 1;
            subTask.setId(id);
            subtasks.put(id, subTask);
            epics.get(subTask.getEpicId()).addSubTask(subTask);
            checkEpicStatus(subTask.getEpicId());
            checkEpicStartEndTime(subTask.getEpicId());
            setPrioritizedTasks(subTask);
        }
        return subTask;
    }

    @Override
    public SubTask updateSubTask(SubTask subTask) {
        if (!checkTime(subTask)) {
            throw new NotValidTaskException(subTask.toString());
        }
        if (subtasks.containsKey(subTask.getId()) && subtasks.get(subTask.getId()).getEpicId().equals(subTask.getEpicId())) {
            removePrioritizedTasks(subTask);
            subtasks.put(subTask.getId(), subTask);
            checkEpicStatus(subTask.getEpicId());
            checkEpicStartEndTime(subTask.getEpicId());
            setPrioritizedTasks(subTask);
        }
        return subTask;
    }

    private void checkEpicStartEndTime(Integer epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);
            List<SubTask> subtasks = getAllSubTasksByEpic(epicId);
            if (!subtasks.isEmpty() && !subtasks.stream().filter(subTask -> subTask.getStartTime() != null).collect(Collectors.toList()).isEmpty()) {
                LocalDateTime startTime = subtasks.stream()
                        .filter(subTask -> subTask.getStartTime() != null)
                        .map(SubTask::getStartTime).min(LocalDateTime::compareTo).get();
                LocalDateTime endTime = subtasks.stream()
                        .filter(subTask -> subTask.getStartTime() != null)
                        .map(SubTask::getEndTime).max(LocalDateTime::compareTo).get();
                Duration duration = Duration.ofMinutes(subtasks.stream()
                        .filter(subTask -> subTask.getDuration() != null)
                        .map(SubTask::getDuration).mapToLong(Duration::toMinutes).sum());
                epic.setStartTime(startTime);
                epic.setEndTime(endTime);
                epic.setDuration(duration);
            } else {
                epic.setStartTime(null);
                epic.setDuration(null);
                epic.setEndTime(null);
            }
        }
    }

    private void checkEpicStatus(Integer epicId) {
        Epic epic = epics.get(epicId);
        if (epic.getSubTasks().isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }
        HashMap<TaskStatus, Integer> countStatuses = new HashMap<>();
        countStatuses.put(TaskStatus.NEW, 0);
        countStatuses.put(TaskStatus.IN_PROGRESS, 0);
        countStatuses.put(TaskStatus.DONE, 0);
        epic.getSubTasks().forEach(subTaskId -> countStatuses.put(subtasks.get(subTaskId).getStatus(), (countStatuses.get(subtasks.get(subTaskId).getStatus()) + 1)));
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
            removePrioritizedTasks(subtasks.get(id));
            inMemoryHistoryManager.remove(id);
            subtasks.remove(id);
        }
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
        epics.get(epicId).getSubTasks().stream().map(subTaskId -> subtasks.get(subTaskId)).forEach(taskList::add);
        return taskList;
    }

    @Override
    public void deleteAllSubTasks() {
        epics.values().forEach(epic -> {
            epic.removeSubTasks();
            checkEpicStatus(epic.getId());
        });
        subtasks.values().forEach(this::removePrioritizedTasks);
        subtasks.keySet().forEach(inMemoryHistoryManager::remove);
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
    public Epic updateEpic(Epic task) {
        if (epics.containsKey(task.getId())) {
            Epic epic = epics.get(task.getId());
            epic.setDescription(task.getDescription());
            epic.setTaskName(task.getTaskName());
        }
        return task;
    }


    @Override
    public void deleteEpic(int id) {
        if (!epics.get(id).getSubTasks().isEmpty()) {
            epics.get(id).getSubTasks().forEach(subTaskId -> {
                removePrioritizedTasks(subtasks.get(subTaskId));
                inMemoryHistoryManager.remove(subTaskId);
                subtasks.remove(subTaskId);
            });
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
        subtasks.values().forEach(this::removePrioritizedTasks);
        subtasks.keySet().forEach(inMemoryHistoryManager::remove);
        subtasks.clear();
        epics.keySet().forEach(inMemoryHistoryManager::remove);
        epics.clear();
    }

    @Override
    public Boolean getCreated() {
        return created;
    }

    protected void reloadTask(Task task, int taskId) {
        reloadId(taskId);
        task.setId(taskId);
        tasks.put(taskId, task);
        setPrioritizedTasks(task);
    }

    protected void reloadEpic(Epic epic, int epicId, TaskStatus status, LocalDateTime startTime, int duration) {
        reloadId(epicId);
        epic.setId(epicId);
        epic.setStatus(status);
        epic.setStartTime(startTime);
        epic.setDuration(Duration.ofMinutes(duration));
        epics.put(epicId, epic);
        setPrioritizedTasks(epic);
    }

    protected void reloadSubTask(SubTask subTask, int subTaskId) {
        if (epics.containsKey(subTask.getEpicId())) {
            reloadId(subTaskId);
            subTask.setId(subTaskId);
            subtasks.put(subTaskId, subTask);
            epics.get(subTask.getEpicId()).addSubTask(subTask);
            checkEpicStatus(subTask.getEpicId());
            checkEpicStartEndTime(subTask.getEpicId());
            setPrioritizedTasks(subTask);
        }
    }

    private void reloadId(int newId) {
        if (newId > id) {
            id = newId;
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }

    public void setPrioritizedTasks(Task task) {
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    public void removePrioritizedTasks(Task task) {
        if (task.getStartTime() != null) {
            prioritizedTasks.remove(task);
        }
    }

    public boolean checkTime(Task task) {
        if (!prioritizedTasks.isEmpty() && task.getStartTime() != null) {
            List<Task> findtask = prioritizedTasks.stream().filter(prioritizedTask -> ((task.getStartTime().isBefore(prioritizedTask.getStartTime()) && task.getEndTime().isAfter(prioritizedTask.getStartTime())) ||
                    (task.getStartTime().isBefore(prioritizedTask.getEndTime()) && task.getEndTime().isAfter(prioritizedTask.getEndTime())))).collect(Collectors.toList());
            if (!findtask.isEmpty()) {
                return false;
            }
        }
        return true;
    }

}
