package ru.practicum.explorewithme.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super("Conflict exception. " + message);
    }
}