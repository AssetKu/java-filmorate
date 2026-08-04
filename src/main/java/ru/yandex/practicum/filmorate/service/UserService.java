package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        validateUser(user);

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        User created = userStorage.create(user);

        log.info("Создан пользователь {}", created);

        return created;
    }

    public User update(User user) {
        validateUser(user);

        if (userStorage.getById(user.getId()) == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        User updated = userStorage.update(user);

        log.info("Обновлен пользователь {}", updated);

        return updated;
    }

    public User getById(int id) {
        User user = userStorage.getById(id);

        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        return user;
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public void addFriend(int id, int friendId) {

        getById(id);
        getById(friendId);

        userStorage.addFriend(id, friendId);

        log.info("Пользователь {} добавил в друзья {}", id, friendId);
    }

    public void removeFriend(int id, int friendId) {

        getById(id);
        getById(friendId);

        userStorage.removeFriend(id, friendId);

        log.info("Пользователь {} удалил из друзей {}", id, friendId);
    }

    public List<User> getFriends(int id) {

        getById(id);

        return userStorage.getFriends(id);
    }

    private void validateUser(User user) {

        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Некорректный email");
        }

        if (user.getLogin() == null
                || user.getLogin().isBlank()
                || user.getLogin().contains(" ")) {
            throw new ValidationException(
                    "Логин не должен содержать пробелы"
            );
        }

        if (user.getBirthday() != null
                && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException(
                    "Дата рождения не может быть в будущем"
            );
        }
    }

    public List<User> getCommonFriends(int id,
                                       int otherId) {

        getById(id);
        getById(otherId);

        return userStorage.getCommonFriends(id, otherId);
    }
}

