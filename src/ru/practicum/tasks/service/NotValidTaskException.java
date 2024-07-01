package ru.practicum.tasks.service;

public class NotValidTaskException extends RuntimeException {

    public NotValidTaskException(String message) {
        super(message);
    }
}
