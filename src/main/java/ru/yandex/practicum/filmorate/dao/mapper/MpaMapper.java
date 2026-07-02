package ru.yandex.practicum.filmorate.dao.mapper;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface MpaMapper {
    
    Mpa makeMpa(ResultSet rs) throws SQLException;
}
