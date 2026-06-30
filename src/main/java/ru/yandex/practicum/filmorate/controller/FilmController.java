package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
        film.setGenres(getGenresByIds(film.getGenres()));

        if (film.getDirectors() == null) {
            film.setDirectors(new HashSet<>());
        }

        film.setId(currentId++);
        films.put(film.getId(), film);

        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм не найден");
        }

        validateFilm(film);

        film.setMpa(getMpaById(film.getMpa().getId()));
        film.setGenres(getGenresByIds(film.getGenres()));

        if (film.getDirectors() == null) {
            film.setDirectors(new HashSet<>());
        }

        films.put(film.getId(), film);

        return film;
    }

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
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
    public List<Film> getPopular(
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(required = false) Integer genreId,
            @RequestParam(required = false) Integer year) {

        return films.values().stream()

                .filter(f -> genreId == null || f.getGenres().stream()
                        .anyMatch(g -> g.getId() == genreId))

                .filter(f -> year == null || f.getReleaseDate().getYear() == year)

                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed())

                .limit(count)
                .toList();
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {

        if (userId <= 0) {
            throw new NotFoundException("Пользователь не найден");
        }

        Film film = films.get(id);
        if (film == null) {
            throw new NotFoundException("Фильм не найден");
        }

        film.getLikes().add(userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable int id, @PathVariable int userId) {

        if (userId <= 0) {
            throw new NotFoundException("Пользователь не найден");
        }

        Film film = films.get(id);
        if (film == null) {
            throw new NotFoundException("Фильм не найден");
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

    private static final List<Genre> GENRES = List.of(
            new Genre(1, "Комедия"),
            new Genre(2, "Драма"),
            new Genre(3, "Мультфильм"),
            new Genre(4, "Триллер"),
            new Genre(5, "Документальный"),
            new Genre(6, "Боевик")
    );

    private Mpa getMpaById(int id) {
        return MPA_LIST.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ValidationException("MPA не найден"));
    }

    private Set<Genre> getGenresByIds(Set<Genre> genres) {
        if (genres == null) {
            return new LinkedHashSet<>();
        }

        return genres.stream()
                .map(g -> GENRES.stream()
                        .filter(genre -> genre.getId() == g.getId())
                        .findFirst()
                        .orElseThrow(() -> new ValidationException("Жанр не найден")))
                .sorted(Comparator.comparingInt(Genre::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private void validateFilm(Film film) {

        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }

        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Описание не может быть длиннее 200 символов");
        }

        if (film.getReleaseDate() != null &&
                film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза раньше 28.12.1895");
        }

        if (film.getDuration() <= 0) {
            throw new ValidationException("Длительность должна быть положительной");
        }
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getFilmsByDirector(
            @PathVariable int directorId,
            @RequestParam String sortBy) {

        List<Film> result = films.values().stream()
                .filter(f -> f.getDirectors().stream()
                        .anyMatch(d -> d.getId() == directorId))
                .toList();

        if ("year".equalsIgnoreCase(sortBy)) {
            result = result.stream()
                    .sorted(Comparator.comparing(Film::getReleaseDate))
                    .toList();
        } else if ("likes".equalsIgnoreCase(sortBy)) {
            result = result.stream()
                    .sorted(Comparator
                            .comparingInt((Film f) -> f.getLikes().size())
                            .reversed()
                            .thenComparingInt(Film::getId))
                    .toList();
        }

        return result;
    }
}
