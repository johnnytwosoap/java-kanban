package ru.practicum.tasks;

import ru.practicum.tasks.model.Epic;
import ru.practicum.tasks.model.SubTask;
import ru.practicum.tasks.model.Task;
import ru.practicum.tasks.model.TaskStatus;
import ru.practicum.tasks.service.Managers;
import ru.practicum.tasks.service.TaskManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Main {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static void main(String[] args) {

        Managers managers = new Managers();
        String filePath = "static/tasks.csv";
        TaskManager taskManager = managers.getFileManager(filePath);

        System.out.println("Поехали!");

        if (taskManager.getCreated()) {

            createTasks(taskManager);
            System.out.println(" ");

            createEpicsAndSubTasks(taskManager);
            System.out.println(" ");

            deleteTasks(taskManager);
            System.out.println(" ");

            createTasks(taskManager);
            System.out.println(" ");

            clearTasks(taskManager);
            System.out.println(" ");

            deleteSubTasksAndEpics(taskManager);
            System.out.println(" ");

            createEpicsAndSubTasks(taskManager);
            System.out.println(" ");

            deleteEpicsAndSubTasks(taskManager);
            System.out.println(" ");

            createEpicsAndSubTasks(taskManager);
            System.out.println(" ");

            clearSubTasks(taskManager);
            System.out.println(" ");

            createEpicsAndSubTasks(taskManager);
            System.out.println(" ");

            clearEpics(taskManager);
            System.out.println(" ");

            createTasks(taskManager);
            System.out.println(" ");

            createEpicsAndSubTasks(taskManager);
            System.out.println(" ");


            updateStatuses(taskManager);
            System.out.println(" ");


            checkHistory(taskManager);
            System.out.println(" ");
        }


        getResult(taskManager);
        System.out.println("Окончание проверки");

    }

    public static void createTasks(TaskManager taskManager) {
        System.out.println("Создаем две задачи");
        Task task1 = new Task("first task","firstTask description", TaskStatus.IN_PROGRESS, LocalDateTime.parse("29-06-2024 03:04:05", formatter), 30);
        Task task2 = new Task("second task","secondTask description", TaskStatus.DONE, null, 0);
        Task firstTask = taskManager.createTask(task1);
        Task secondTask = taskManager.createTask(task2);
        System.out.println("Первая задача " + firstTask.toString());
        System.out.println("Вторая задача " + secondTask.toString());
    }


    public static void createEpicsAndSubTasks(TaskManager taskManager) {
        System.out.println("Создаем два эпика с подзадачами");
        Epic epic1 = new Epic("first epic","firstEpic description");
        Epic firstEpic = taskManager.createEpic(epic1);
        System.out.println("Первый эпик без подзадач " + firstEpic.toString());
        SubTask subTask1 = new SubTask("first subtask", "firstSubTask description", TaskStatus.DONE, firstEpic.getId(), LocalDateTime.parse("28-06-2024 03:04:05", formatter), 10);
        SubTask firstSubTask = taskManager.createSubTask(subTask1);
        System.out.println("Первая подзадача " + firstSubTask.toString());

        Epic epic2 = new Epic("second epic","secondEpic description");
        Epic secondEpic = taskManager.createEpic(epic2);

        SubTask subTask2 = new SubTask("second subtask", "secondSubTask description", TaskStatus.NEW, secondEpic.getId(), LocalDateTime.parse("28-06-2024 03:03:15", formatter), 5);
        SubTask secondSubTask = taskManager.createSubTask(subTask2);
        System.out.println("Вторая подзадача " + secondSubTask.toString());

        SubTask subTask3 = new SubTask("third subtask", "thirdSubTask description", TaskStatus.NEW, secondEpic.getId(), null , 0);
        SubTask thirdSubTask = taskManager.createSubTask(subTask3);
        System.out.println("Третья подзадача " + thirdSubTask.toString());
        System.out.println("Первый эпик c подзадачами " + firstEpic);
        System.out.println("Все подзадачи " + taskManager.getAllSubTasks());
        System.out.println("Подзадачи второго эпика " + taskManager.getAllSubTasksByEpic(secondEpic.getId()));
    }

    public static void deleteTasks(TaskManager taskManager) {
        System.out.println("Удаляем задания по очереди");
        System.out.println("Список задач до удаления" + taskManager.getAllTasks());
        for (Task task: taskManager.getAllTasks()) {
            taskManager.deleteTask(task.getId());
        }
        System.out.println("Список задач после удаления" + taskManager.getAllTasks());
    }

    public static void clearTasks(TaskManager taskManager) {
        System.out.println("Удаляем все задания");
        System.out.println("Список задач до удаления" + taskManager.getAllTasks());
        taskManager.deleteAllTasks();
        System.out.println("Список задач после удаления" + taskManager.getAllTasks());
    }

    public static void deleteSubTasksAndEpics(TaskManager taskManager) {
        System.out.println("Удаляем подзадания по очереди");
        System.out.println("Список подзадач до удаления" + taskManager.getAllSubTasks());
        for (SubTask task: taskManager.getAllSubTasks()) {
            taskManager.deleteSubTask(task.getId());
        }
        System.out.println("Список подзадач после удаления" + taskManager.getAllSubTasks());
        System.out.println("Список эпиков после удаления подзадач " + taskManager.getAllEpic());

        System.out.println("Удаляем эпики по очереди");
        for (Epic task: taskManager.getAllEpic()) {
            taskManager.deleteEpic(task.getId());
        }
        System.out.println("Список эпиков после удаления " + taskManager.getAllEpic());
    }

    public static void deleteEpicsAndSubTasks(TaskManager taskManager) {
        System.out.println("Удаляем эпики по очереди");
        System.out.println("Список эпиков до удаления " + taskManager.getAllEpic());
        for (Epic task: taskManager.getAllEpic()) {
            taskManager.deleteEpic(task.getId());
        }
        System.out.println("Список эпиков после удаления " + taskManager.getAllEpic());
        System.out.println("Список подзадач после удаления эпиков " + taskManager.getAllSubTasks());
    }

    public static void clearSubTasks(TaskManager taskManager) {
        System.out.println("Удаляем все подзадания");
        System.out.println("Список подзадач до удаления" + taskManager.getAllSubTasks());
        taskManager.deleteAllSubTasks();
        System.out.println("Список подзадач после удаления" + taskManager.getAllSubTasks());
        System.out.println("Список эпиков после удаления подзадач " + taskManager.getAllEpic());
    }

    public static void clearEpics(TaskManager taskManager) {
        System.out.println("Удаляем все эпики");
        System.out.println("Список эпиков до удаления " + taskManager.getAllEpic());
        taskManager.deleteAllEpics();
        System.out.println("Список эпиков после удаления " + taskManager.getAllEpic());
        System.out.println("Список подзадач после удаления эпиков " + taskManager.getAllSubTasks());
    }

    public static void updateStatuses(TaskManager taskManager) {
        System.out.println("Изменяем статусы заданий");
        Task task = taskManager.getAllTasks().getFirst();
        Task task1 = taskManager.getAllTasks().getLast();
        task.setStatus(TaskStatus.DONE);
        task1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task);
        taskManager.updateTask(task1);
        System.out.println(taskManager.getAllTasks());


        System.out.println("Изменяем статусы подзаданий");
        Epic epic = taskManager.getAllEpic().getLast();
        System.out.println("Начальное состояние " + epic);
        List<SubTask> subTasks = taskManager.getAllSubTasksByEpic(epic.getId());
        SubTask subTask = subTasks.getFirst();
        SubTask subTask1 = subTasks.getLast();
        subTask.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(subTask);
        System.out.println("Одно подзадание статус в процессе " + epic);
        subTask.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTask);
        System.out.println("Одно подзадание статус сделано " + epic);
        subTask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(subTask1);
        System.out.println("Сделано + в процессе " + epic);
        subTask1.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTask1);
        System.out.println("Два подзадания статус сделано " + epic);
    }

    public static void checkHistory(TaskManager taskManager) {
        System.out.println("Проверяем историю обращений");
        taskManager.getTask(taskManager.getAllTasks().getFirst().getId());
        //taskManager.deleteTask(taskManager.getAllTasks().getFirst().getId());
        taskManager.getSubTask(taskManager.getAllSubTasks().getFirst().getId());
        taskManager.getEpic(taskManager.getAllEpic().getFirst().getId());
        taskManager.getTask(taskManager.getAllTasks().getFirst().getId());
        taskManager.getSubTask(taskManager.getAllSubTasks().getFirst().getId());
        taskManager.getEpic(taskManager.getAllEpic().getFirst().getId());
        taskManager.getTask(taskManager.getAllTasks().getFirst().getId());
        taskManager.getSubTask(taskManager.getAllSubTasks().getFirst().getId());
        taskManager.getEpic(taskManager.getAllEpic().getFirst().getId());
        taskManager.getTask(taskManager.getAllTasks().getFirst().getId());
        taskManager.getSubTask(taskManager.getAllSubTasks().getFirst().getId());
        taskManager.getEpic(taskManager.getAllEpic().getFirst().getId());
        System.out.println(taskManager.getHistory());
    }

    public static void getResult(TaskManager taskManager) {
        System.out.println("Задания после всех проверок");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println("Task " + task.toString());
        }
        for (Epic task : taskManager.getAllEpic()) {
            System.out.println("Epic " + task.toString());
        }
        for (SubTask task : taskManager.getAllSubTasks()) {
            System.out.println("SubTask " + task.toString());
        }
        System.out.println("Задания после всех проверок по приоритету");
        for (Object task : taskManager.getPrioritizedTasks()) {
            System.out.println(task.toString());

        }
    }
}
