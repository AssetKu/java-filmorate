package ru.yandex.practicum.filmorate.dao.mapper;

import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface GenreMapper {
    
    Genre makeGenre(ResultSet rs) throws SQLException;
}
