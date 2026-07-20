package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@SpringBootTest
class FilmControllerTest {

    @Autowired
    private FilmController controller;

    @Test
    void shouldThrowIfNameEmpty() {
        Film film = new Film();
        film.setName(" ");
        film.setDescription("test");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(100);

        Assertions.assertThrows(
                ValidationException.class,
                () -> controller.create(film)
        );
    }

    @Test
    void shouldThrowIfDescriptionTooLong() {
        Film film = new Film();
        film.setName("Film");
        film.setDescription("a".repeat(201));
        film.setReleaseDate(LocalDate.now());
        film.setDuration(100);

        Assertions.assertThrows(
                ValidationException.class,
                () -> controller.create(film)
        );
    }

    @Test
    void shouldThrowIfReleaseDateTooEarly() {
        Film film = new Film();
        film.setName("Film");
        film.setDescription("test");
        film.setReleaseDate(LocalDate.of(1800, 1, 1));
        film.setDuration(100);

        Assertions.assertThrows(
                ValidationException.class,
                () -> controller.create(film)
        );
    }

    @Test
    void shouldThrowIfDurationNegative() {
        Film film = new Film();
        film.setName("Film");
        film.setDescription("test");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(-10);

        Assertions.assertThrows(
                ValidationException.class,
                () -> controller.create(film)
        );
    }

    @Test
    void shouldCreateValidFilm() {
        Film film = new Film();
        film.setName("Film");
        film.setDescription("Good");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        Film created = controller.create(film);

        Assertions.assertNotNull(created);
        Assertions.assertTrue(created.getId() > 0);
        Assertions.assertEquals("Film", created.getName());
    }
}