package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import(FilmDbStorage.class)
class FilmDbStorageTest {

    private final FilmDbStorage filmStorage;

    @Test
    void shouldCreateAndFindFilm() {

        Film film = new Film();
        film.setName("Film");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        film.setRate(0);

        Mpa mpa = new Mpa();
        mpa.setId(1);

        film.setMpa(mpa);

        filmStorage.create(film);

        Film savedFilm = filmStorage.getById(film.getId());

        assertThat(savedFilm).isNotNull();
        assertThat(savedFilm.getId()).isEqualTo(film.getId());
        assertThat(savedFilm.getName()).isEqualTo("Film");
    }
}