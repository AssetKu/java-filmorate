package ru.yandex.practicum.filmorate.exception;

public class RatedUserNotFoundException extends RuntimeException{
    public RatedUserNotFoundException(String message) {
        super(message);
    }
}
