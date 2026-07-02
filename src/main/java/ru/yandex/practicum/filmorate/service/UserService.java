package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendshipDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    
    private final UserDao userDao;
    private final FriendshipDao friendshipDao;
    
    /**
     * Возвращает пользователя по его id
     *
     * @param id - id пользователя
     * @return - пользователь
     */
    public User getUser(Long id) {
        return userDao.get(id);
    }
    
    /**
     * Назначает пользователю id и добавляет его в хранилище
     *
     * @param user - пользователь
     */
    public void createUser(User user) {
        user.setId(userDao.save(user));
    }
    
    /**
     * Обновляет данные пользователя
     *
     * @param user - пользователь, которого нужно обновить
     */
    public void updateUser(User user) {
        userDao.update(user);
    }
    
    /**
     * Удаляет пользователя из хранилища
     *
     * @param id - id пользователя в хранилище
     */
    public void deleteUser(Long id) {
        userDao.delete(id);
    }
    
    /**
     * Возвращает список пользователей из хранилища
     *
     * @return - список пользователей
     */
    public List<User> getUsers() {
        return userDao.getUsers();
    }
    
    /**
     * Добавляет пользователей в друзья друг другу
     *
     * @param userId   - id первого пользователя
     * @param friendId - id второго пользователя
     */
    public void friending(Long userId, Long friendId) {
        checkUserExist(userId);
        checkUserExist(friendId);
        
        friendshipDao.friending(userId, friendId);
    }
    
    /**
     * Удаляет из друзей пользователей друг у друга
     *
     * @param userId   - id первого пользователя
     * @param friendId - id второго пользователя
     */
    public void unfriending(Long userId, Long friendId) {
        if (userDao.isUserExist(userId) && userDao.isUserExist(friendId)) {
            friendshipDao.unfriending(userId, friendId);
        } else {
            throw new UserNotFoundException(String.format("Пользователей с id=%s и/или c id=%s не найдено.",
                userId, friendId));
        }
    }
    
    /**
     * Возвращает список друзей пользователя
     *
     * @param userId - id пользователя
     * @return - список друзей пользователя
     */
    public List<User> getFriends(Long userId) {
        checkUserExist(userId);
        
        return friendshipDao.getFriends(userId);
    }
    
    /**
     * Возвращает список общих друзей у пользователей
     *
     * @param firstId  - id первого пользователя
     * @param secondId - id второго пользователя
     * @return - список общих друзей пользователей
     */
    public List<User> getCommonFriends(Long firstId, Long secondId) {
        checkUserExist(firstId);
        checkUserExist(secondId);
        
        return friendshipDao.getCommonFriends(firstId, secondId);
    }
    
    //++++++++++++++++++
    
    private void checkUserExist(Long id) {
        if (!userDao.isUserExist(id)) {
            throw new UserNotFoundException(String.format("Пользователь с id=%s не найден.", id));
        }
    }
    
    /**
     * Очищает хранилище пользователей и сбрасывает счетчик id
     */
    public void usersClear() {
        userDao.usersClear();
    }
}
