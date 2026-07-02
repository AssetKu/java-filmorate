package ru.yandex.practicum.filmorate.dao.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.dao.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.dao.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class FilmDbMapper implements FilmMapper {
    
    final MpaMapper mpaMapper;
    final GenreMapper genreMapper;
    final JdbcTemplate jdbcTemplate;
    
    @Override
    public Film makeFilm(ResultSet rs) throws SQLException {
        // FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA_RATE
        
        Long id = rs.getLong("FILM_ID");
        String name = rs.getString("FILM_NAME");
        String description = rs.getString("DESCRIPTION");
        LocalDate releaseDate = rs.getDate("RELEASE_DATE").toLocalDate();
        Integer duration = rs.getInt("DURATION");
        Integer rate = rs.getInt("RATE");
        Integer mpaId = rs.getInt("MPA_RATE");
        
        String sqlMpaQuery = "select " +
            "MPA_ID, " +
            "MPA_NAME, " +
            "MPA_DESCRIPTION " +
            "from MPAS " +
            "where MPA_ID = ?";
        Mpa mpaRate = Objects.requireNonNull(jdbcTemplate.queryForObject(sqlMpaQuery, (rsMpa, rowNum) -> mpaMapper.makeMpa(rsMpa), mpaId));
        
        String sqlGenreQuery = "select " +
            "g.GENRE_ID, " +
            "g.GENRE_NAME " +
            "from FILMGENRES f " +
            "join GENRES g on g.GENRE_ID = f.GENRE_ID " +
            "where FILM_ID = ? " +
            "order by GENRE_ID";
        List<Genre> genres = jdbcTemplate.query(sqlGenreQuery, (rsGenre, rowNum) -> genreMapper.makeGenre(rsGenre), id);
        
        return new Film(id, name, description, releaseDate, duration, rate, mpaRate, genres);
    }
}
