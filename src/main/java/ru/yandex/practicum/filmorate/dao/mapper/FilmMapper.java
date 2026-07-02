package ru.yandex.practicum.filmorate.dao.mapper;

import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface FilmMapper {
    
    Film makeFilm(ResultSet rs) throws SQLException;
}
