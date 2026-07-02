package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Month;

public class AfterFirstFilmValidator implements ConstraintValidator<AfterFirstFilm, LocalDate> {
    
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        LocalDate firstFilmDate = LocalDate.of(1895, Month.DECEMBER, 28);
        return value != null && value.isAfter(firstFilmDate);
    }
}
