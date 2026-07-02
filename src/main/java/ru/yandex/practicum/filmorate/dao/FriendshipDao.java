package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipDao {
    
    boolean friending(Long userId, Long friendId);
    
    boolean unfriending(Long userId, Long friendId);
    
    List<User> getFriends(Long userId);
    
    List<User> getCommonFriends(Long firstId, Long secondId);
}
