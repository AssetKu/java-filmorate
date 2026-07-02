package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDao {
    
    Long save(User user);
    
    User get(Long id);
    
    void update(User user);
    
    void delete(Long id);
    
    List<User> getUsers();
    
    //++++++++++++++++
    void usersClear();
    
    boolean isUserExist(Long id);
}
