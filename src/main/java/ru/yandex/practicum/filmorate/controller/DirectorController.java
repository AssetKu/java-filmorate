package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.*;

@RestController
@RequestMapping("/directors")
public class DirectorController {

    private final Map<Integer, Director> directors = new HashMap<>();
    private int currentId = 1;

    @GetMapping
    public Collection<Director> getAll() {
        return directors.values();
    }

    @GetMapping("/{id}")
    public Director getById(@PathVariable int id) {
        Director director = directors.get(id);
        if (director == null) {
            throw new NotFoundException("Режиссер не найден");
        }
        return director;
    }

    @PostMapping
    public Director create(@RequestBody Director director) {
        if (director.getName() == null || director.getName().isBlank()) {
            throw new ValidationException("Имя режиссера не может быть пустым");
        }

        director.setId(currentId++);
        directors.put(director.getId(), director);

        return director;
    }

    @PutMapping
    public Director update(@RequestBody Director director) {
        if (director.getId() <= 0) {
            throw new ValidationException("Id должен быть указан");
        }

        if (!directors.containsKey(director.getId())) {
            throw new NotFoundException("Режиссер не найден");
        }

        if (director.getName() == null || director.getName().isBlank()) {
            throw new ValidationException("Имя режиссера не может быть пустым");
        }

        directors.put(director.getId(), director);

        return director;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        if (!directors.containsKey(id)) {
            throw new NotFoundException("Режиссер не найден");
        }

        directors.remove(id);
    }
}