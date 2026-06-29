package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController {

    private static final List<Mpa> MPA_LIST = List.of(
            new Mpa(1, "G"),
            new Mpa(2, "PG"),
            new Mpa(3, "PG-13"),
            new Mpa(4, "R"),
            new Mpa(5, "NC-17")
    );

    @GetMapping
    public List<Mpa> getAll() {
        return MPA_LIST;
    }

    @GetMapping("/{id}")
    public Mpa getById(@PathVariable int id) {
        return MPA_LIST.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("MPA не найден"));
    }
}