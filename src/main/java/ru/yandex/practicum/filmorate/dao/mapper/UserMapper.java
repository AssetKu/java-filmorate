package ru.yandex.practicum.filmorate.dao.mapper;

import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface UserMapper {
    
    User makeUser(ResultSet rs) throws SQLException;
}
