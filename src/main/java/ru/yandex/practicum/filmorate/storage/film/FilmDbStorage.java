package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;
import java.util.List;

@Component("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Film> mapper = (rs, rowNum) -> {

        Film film = new Film();

        film.setId(rs.getInt("FILM_ID"));
        film.setName(rs.getString("FILM_NAME"));
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setReleaseDate(
                rs.getDate("RELEASE_DATE").toLocalDate()
        );
        film.setDuration(rs.getInt("DURATION"));
        film.setRate(rs.getInt("RATE"));

        return film;
    };

    @Override
    public Film create(Film film) {

        String sql = """
                INSERT INTO FILMS
                (FILM_NAME,
                 DESCRIPTION,
                 RELEASE_DATE,
                 DURATION,
                 RATE,
                 MPA_RATE)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(
                sql,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId()
        );

        Integer id = jdbcTemplate.queryForObject(
                "SELECT MAX(FILM_ID) FROM FILMS",
                Integer.class
        );

        film.setId(id);

        return film;
    }

    @Override
    public Film update(Film film) {

        String sql = """
                UPDATE FILMS
                SET FILM_NAME=?,
                    DESCRIPTION=?,
                    RELEASE_DATE=?,
                    DURATION=?,
                    RATE=?,
                    MPA_RATE=?
                WHERE FILM_ID=?
                """;

        jdbcTemplate.update(
                sql,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId()
        );

        return film;
    }

    @Override
    public Film getById(int id) {

        List<Film> films = jdbcTemplate.query(
                """
                SELECT *
                FROM FILMS
                WHERE FILM_ID = ?
                """,
                mapper,
                id
        );

        return films.isEmpty()
                ? null
                : films.get(0);
    }

    @Override
    public List<Film> getAll() {

        return jdbcTemplate.query(
                """
                SELECT *
                FROM FILMS
                """,
                mapper
        );
    }
}