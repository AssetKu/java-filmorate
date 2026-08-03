package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }


    public Film create(Film film) {
        validateFilm(film);

        Film created = filmStorage.create(film);

        log.info("Создан фильм {}", created);

        return created;
    }

    public Film update(Film film) {
        validateFilm(film);

        if (filmStorage.getById(film.getId()) == null) {
            throw new NotFoundException("Фильм не найден");
        }

        Film updated = filmStorage.update(film);

        log.info("Обновлен фильм {}", updated);

        return updated;
    }

    public Film getById(int id) {
        Film film = filmStorage.getById(id);

        if (film == null) {
            throw new NotFoundException("Фильм не найден");
        }

        return film;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public void addLike(int filmId, int userId) {
        Film film = getById(filmId);

        if (userStorage.getById(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        film.getLikes().add(userId);

        log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
    }

    public void removeLike(int filmId, int userId) {
        Film film = getById(filmId);

        if (userStorage.getById(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        film.getLikes().remove(userId);

        log.info("Пользователь {} удалил лайк у фильма {}", userId, filmId);
    }

    public List<Film> getPopular(int count) {
        return filmStorage.getAll().stream()
                .sorted(
                        Comparator.comparingInt(
                                (Film film) -> film.getLikes().size()
                        ).reversed()
                )
                .limit(count)
                .toList();
    }

    private void validateFilm(Film film) {

        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException(
                    "Название не может быть пустым"
            );
        }

        if (film.getDescription() != null
                && film.getDescription().length() > 200) {
            throw new ValidationException(
                    "Описание не может быть длиннее 200 символов"
            );
        }

        if (film.getReleaseDate() != null
                && film.getReleaseDate().isBefore(
                LocalDate.of(1895, 12, 28))) {
            throw new ValidationException(
                    "Дата релиза раньше 28.12.1895"
            );
        }

        if (film.getDuration() <= 0) {
            throw new ValidationException(
                    "Длительность должна быть положительной"
            );
        }
    }
}