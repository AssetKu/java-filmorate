package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAll() {

        String sql = """
                SELECT GENRE_ID,
                       GENRE_NAME
                FROM GENRES
                ORDER BY GENRE_ID
                """;

        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new Genre(
                        rs.getInt("GENRE_ID"),
                        rs.getString("GENRE_NAME")
                ));
    }

    @Override
    public Genre getById(int id) {

        String sql = """
                SELECT GENRE_ID,
                       GENRE_NAME
                FROM GENRES
                WHERE GENRE_ID = ?
                """;

        List<Genre> genres = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Genre(
                        rs.getInt("GENRE_ID"),
                        rs.getString("GENRE_NAME")
                ),
                id
        );

        if (genres.isEmpty()) {
            throw new NotFoundException("Жанр не найден");
        }

        return genres.get(0);
    }
}