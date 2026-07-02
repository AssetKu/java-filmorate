package ru.yandex.practicum.filmorate.dao.mapper.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GenreDbMapper implements GenreMapper {
    
    @Override
    public Genre makeGenre(ResultSet rs) throws SQLException {
        
        Long id = rs.getLong("GENRE_ID");
        String name = rs.getString("GENRE_NAME");
        
        return new Genre(id, name);
    }
}
