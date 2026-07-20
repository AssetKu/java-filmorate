package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film create(Film film) {
        Film created = filmStorage.create(film);
        log.info("Создан фильм {}", created);
        return created;
    }

    public Film update(Film film) {
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
        log.info("Запрошен список фильмов");
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
        log.info("Запрошен список популярных фильмов. count={}", count);

        return filmStorage.getAll().stream()
                .sorted(
                        Comparator.comparingInt(
                                (Film film) -> film.getLikes().size()
                        ).reversed()
                )
                .limit(count)
                .toList();
    }
}