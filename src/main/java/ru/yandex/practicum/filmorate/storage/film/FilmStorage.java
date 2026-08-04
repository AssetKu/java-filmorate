package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    Film getById(int id);

    List<Film> getAll();

    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);

    Set<Integer> getLikes(int filmId);
}