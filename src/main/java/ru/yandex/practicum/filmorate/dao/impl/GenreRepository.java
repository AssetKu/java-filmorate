package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenreRepository implements GenreDao {
    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;
    
    @Override
    public Genre getGenre(Long id) {
        String sqlQuery = "select " +
            "GENRE_ID, " +
            "GENRE_NAME " +
            "from GENRES " +
            "where GENRE_ID = ?";
        
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> genreMapper.makeGenre(rs), id)
            .stream()
            .findAny()
            .orElseThrow(() -> {throw new GenreNotFoundException(String
                .format("Жанр с id=%s не найден в базе.", id));
        });
    }
    
    @Override
    public List<Genre> getAllGenres() {
        String sqlQuery = "select " +
            "GENRE_ID, " +
            "GENRE_NAME " +
            "from GENRES " +
            "order by GENRE_ID";
        
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> genreMapper.makeGenre(rs));
    }
}
