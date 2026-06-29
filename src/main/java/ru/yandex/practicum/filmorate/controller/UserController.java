package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int currentId = 1;

    @PostMapping
    public User create(@RequestBody User user) {
        validateUser(user);

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        user.setId(currentId++);
        users.put(user.getId(), user);

        log.info("Создан пользователь: {}", user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        if (user.getId() <= 0) {
            throw new ValidationException("Id должен быть указан");
        }

        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь не найден");
        }

        validateUser(user);

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);

        log.info("Обновлен пользователь: {}", user);
        return user;
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable int id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return user;
    }


    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        User user = users.get(id);
        User friend = users.get(friendId);

        if (user == null || friend == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        user.getFriends().add(friendId);
        friend.getFriends().add(id);
    }


    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable int id, @PathVariable int friendId) {
        User user = users.get(id);
        User friend = users.get(friendId);

        if (user == null || friend == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
    }


    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        User user = users.get(id);

        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        return user.getFriends().stream()
                .map(users::get)
                .toList();
    }


    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        User user = users.get(id);
        User other = users.get(otherId);

        if (user == null || other == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        return user.getFriends().stream()
                .filter(other.getFriends()::contains)
                .map(users::get)
                .toList();
    }

    @GetMapping
    public Collection<User> getAll() {
        return new ArrayList<>(users.values());
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Ошибка валидации email");
            throw new ValidationException("Некорректный email");
        }

        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Ошибка валидации login");
            throw new ValidationException("Логин не должен содержать пробелы");
        }

        if (user.getBirthday() != null &&
                user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ошибка валидации birthday");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}