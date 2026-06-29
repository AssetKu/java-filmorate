package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int currentId = 1;


    @PostMapping
    public Film create(@RequestBody Film film) {
        validateFilm(film);

        film.setMpa(getMpaById(film.getMpa().getId()));

        film.setId(currentId++);
        films.put(film.getId(), film);

        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        if (film.getId() <= 0) {
            throw new ValidationException("Id должен быть указан");
        }

        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм не найден");
        }

        validateFilm(film);

        // ✅ И ЗДЕСЬ ТОЖЕ
        film.setMpa(getMpaById(film.getMpa().getId()));

        films.put(film.getId(), film);

        log.info("Обновлен фильм: {}", film);
        return film;
    }

    @GetMapping
    public Collection<Film> getAll() {
        return new ArrayList<>(films.values());
    }


    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Ошибка валидации: пустое имя");
            throw new ValidationException("Название не может быть пустым");
        }

        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.error("Ошибка валидации: описание > 200");
            throw new ValidationException("Описание не может быть длиннее 200 символов");
        }

        if (film.getReleaseDate() != null &&
                film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Ошибка валидации: дата релиза");
            throw new ValidationException("Дата релиза раньше 28.12.1895");
        }

        if (film.getDuration() <= 0) {
            log.error("Ошибка валидации: длительность");
            throw new ValidationException("Длительность должна быть положительной");
        }
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable int id) {
        Film film = films.get(id);
        if (film == null) {
            throw new NotFoundException("Фильм не найден");
        }
        return film;
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        return films.values().stream()
                .sorted(Comparator.comparingInt(f -> -f.getLikes().size()))
                .limit(count)
                .toList();
    }


    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        Film film = films.get(id);

        if (film == null) {
            throw new NotFoundException("Фильм не найден");
        }

        if (userId <= 0) {
            throw new NotFoundException("Пользователь не найден");
        }

        film.getLikes().add(userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable int id, @PathVariable int userId) {
        Film film = films.get(id);

        if (film == null) {
            throw new NotFoundException("Фильм не найден");
        }

        if (userId <= 0) {
            throw new NotFoundException("Пользователь не найден");
        }

        film.getLikes().remove(userId);
    }

    private static final List<Mpa> MPA_LIST = List.of(
            new Mpa(1, "G"),
            new Mpa(2, "PG"),
            new Mpa(3, "PG-13"),
            new Mpa(4, "R"),
            new Mpa(5, "NC-17")
    );

    private Mpa getMpaById(int id) {
        return MPA_LIST.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ValidationException("MPA не найден"));
    }
}
