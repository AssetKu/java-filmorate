package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

        film.setMpa(
                getMpa(rs.getInt("MPA_RATE"))
        );

        film.setGenres(
                getGenres(rs.getInt("FILM_ID"))
        );

        return film;
    };

    @Override
    public Film create(Film film) {

        String sql = """
                INSERT INTO FILMS (
                    FILM_NAME,
                    DESCRIPTION,
                    RELEASE_DATE,
                    DURATION,
                    RATE,
                    MPA_RATE
                )
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

        saveGenres(film);

        return getById(id);
    }

    @Override
    public Film update(Film film) {

        String sql = """
                UPDATE FILMS
                SET FILM_NAME = ?,
                    DESCRIPTION = ?,
                    RELEASE_DATE = ?,
                    DURATION = ?,
                    RATE = ?,
                    MPA_RATE = ?
                WHERE FILM_ID = ?
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

        jdbcTemplate.update(
                "DELETE FROM FILMGENRES WHERE FILM_ID = ?",
                film.getId()
        );

        saveGenres(film);

        return getById(film.getId());
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
                ORDER BY FILM_ID
                """,
                mapper
        );
    }

    private void saveGenres(Film film) {

        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }

        for (Genre genre : film.getGenres()) {

            jdbcTemplate.update(
                    """
                    INSERT INTO FILMGENRES (FILM_ID, GENRE_ID)
                    VALUES (?, ?)
                    """,
                    film.getId(),
                    genre.getId()
            );
        }
    }

    private Set<Genre> getGenres(int filmId) {

        return new LinkedHashSet<>(
                jdbcTemplate.query(
                        """
                        SELECT g.GENRE_ID,
                               g.GENRE_NAME
                        FROM FILMGENRES fg
                        JOIN GENRES g
                          ON fg.GENRE_ID = g.GENRE_ID
                        WHERE fg.FILM_ID = ?
                        ORDER BY g.GENRE_ID
                        """,
                        (rs, rowNum) -> new Genre(
                                rs.getInt("GENRE_ID"),
                                rs.getString("GENRE_NAME")
                        ),
                        filmId
                )
        );
    }

    private Mpa getMpa(int mpaId) {

        return jdbcTemplate.queryForObject(
                """
                SELECT MPA_ID,
                       MPA_NAME
                FROM MPAS
                WHERE MPA_ID = ?
                """,
                (rs, rowNum) -> new Mpa(
                        rs.getInt("MPA_ID"),
                        rs.getString("MPA_NAME")
                ),
                mpaId
        );
    }
}