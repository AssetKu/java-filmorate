package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AfterFirstFilmValidator.class)
@Documented
public @interface AfterFirstFilm {
    
    String message() default "AfterFirstFilmValidator.invalid";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
