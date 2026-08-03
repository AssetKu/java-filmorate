package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
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

        User user = getById(id);
        User friend = getById(friendId);

        user.getFriends().put(friendId,
                FriendshipStatus.UNCONFIRMED);

        if (friend.getFriends().containsKey(id)) {
            user.getFriends().put(friendId,
                    FriendshipStatus.CONFIRMED);

            friend.getFriends().put(id,
                    FriendshipStatus.CONFIRMED);
        }
    }

    public void removeFriend(int id, int friendId) {

        User user = getById(id);
        User friend = getById(friendId);

        user.getFriends().remove(friendId);

        if (friend.getFriends().containsKey(id)) {
            friend.getFriends().put(id,
                    FriendshipStatus.UNCONFIRMED);
        }
    }

    public List<User> getFriends(int id) {

        User user = getById(id);

        return user.getFriends()
                .keySet()
                .stream()
                .map(this::getById)
                .toList();
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

        User user = getById(id);
        User other = getById(otherId);

        return user.getFriends()
                .keySet()
                .stream()
                .filter(other.getFriends().keySet()::contains)
                .map(this::getById)
                .toList();
    }
}

