package ru.yandex.practicum.filmorate.exception;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
@NoArgsConstructor
public class ErrorHandler {
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ErrorResponse validationException(ValidationException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage(), "Ошибка валидации данных.");
    }
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ErrorResponse constraintViolationException(ConstraintViolationException e) {
        log.warn(e.getMessage());
        return new ErrorResponse("Ошибка валидации данных", e.getMessage());
    }
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse userNotFoundException(UserNotFoundException e) {
        log.warn(e.getMessage());
        return new ErrorResponse("Ошибка", e.getMessage());
    }
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse filmNotFoundException(FilmNotFoundException e) {
        log.warn(e.getMessage());
        return new ErrorResponse("Ошибка", e.getMessage());
    }
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse ratedUserNotFoundException(RatedUserNotFoundException e) {
        log.warn(e.getMessage());
        return new ErrorResponse("Ошибка", e.getMessage());
    }
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse mpaNotFoundException(MpaNotFoundException e) {
        log.warn(e.getMessage());
        return new ErrorResponse("Ошибка", e.getMessage());
    }
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse genreNotFoundException(GenreNotFoundException e) {
        log.warn(e.getMessage());
        return new ErrorResponse("Ошибка", e.getMessage());
    }
}
