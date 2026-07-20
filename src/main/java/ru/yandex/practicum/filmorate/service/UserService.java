package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User create(User user) {

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        User created = userStorage.create(user);

        log.info("Создан пользователь {}", created);

        return created;
    }

    public User update(User user) {

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
        log.info("Запрошен список пользователей");
        return userStorage.getAll();
    }

    public void addFriend(int id, int friendId) {

        User user = getById(id);
        User friend = getById(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(id);

        log.info("Пользователь {} добавил в друзья {}", id, friendId);
    }

    public void removeFriend(int id, int friendId) {

        User user = getById(id);
        User friend = getById(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);

        log.info("Пользователь {} удалил из друзей {}", id, friendId);
    }

    public List<User> getFriends(int id) {

        User user = getById(id);

        log.info("Запрошен список друзей пользователя {}", id);

        return user.getFriends().stream()
                .map(this::getById)
                .toList();
    }

    public List<User> getCommonFriends(int id, int otherId) {

        User user = getById(id);
        User otherUser = getById(otherId);

        log.info("Запрошены общие друзья пользователей {} и {}", id, otherId);

        return user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)
                .map(this::getById)
                .toList();
    }
}