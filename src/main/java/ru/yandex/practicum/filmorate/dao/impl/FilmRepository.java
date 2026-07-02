package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.dao.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.dao.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
@Primary
@RequiredArgsConstructor
public class FilmRepository implements FilmDao {
    
    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;
    private final MpaMapper mpaMapper;
    private final GenreMapper genreMapper;
    
    @Override
    public void save(Film film) {
        // Запрос для сохранения в таблице FILMS
        String sqlQuery = "insert into FILMS (" +
                "FILM_NAME, " +
                "DESCRIPTION, " +
                "RELEASE_DATE, " +
                "DURATION, " +
                "RATE, " +
                "MPA_RATE) " +
                "values (?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getRate());
            stmt.setLong(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        
        // Заполняем MPA в film
        setMpaToFilm(film);

        // Запрос для сохранения в таблице FILMGENRES
        if (film.getGenres() != null) {
            StringBuilder sqlGenreQuery = new StringBuilder("insert into FILMGENRES (FILM_ID, GENRE_ID) values");
            
            for (Genre item : film.getGenres()) {
                sqlGenreQuery.append(String.format(" ('%s', '%s'),", film.getId(), item.getId()));
            }
            
            sqlGenreQuery.setCharAt(sqlGenreQuery.length() - 1, ';');
            
            jdbcTemplate.update(sqlGenreQuery.toString());
        }
        
        // Заполняем genres в film
        setGenresToFilm(film);
    }
    
    @Override
    public void delete(Long id) {
        String sqlQuery = "delete from FILMS where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }
    
    @Override
    public void update(Film film) {
        // Запрос для обновления таблицы FILMS
        String sqlQuery = "update FILMS set " +
                "FILM_NAME = ?, " +
                "DESCRIPTION = ?, " +
                "RELEASE_DATE = ?, " +
                "DURATION = ?, " +
                "MPA_RATE = ? " +
                "where FILM_ID = ?";
        
        if (isFilmExist(film.getId())) {
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId()
            );
            
            // Запрос для сохранения в таблице FILMGENRES
            if (film.getGenres() != null) {
                String sqlGenreDeleteQuery = "delete from FILMGENRES where FILM_ID = ?";
                jdbcTemplate.update(sqlGenreDeleteQuery, film.getId());
                
                if (film.getGenres().size() > 0) {
                    StringBuilder sqlGenreQuery = new StringBuilder("insert into FILMGENRES (FILM_ID, GENRE_ID) values");
                    
                    for (Genre item : film.getGenres()) {
                        sqlGenreQuery.append(String.format(" ('%s', '%s'),", film.getId(), item.getId()));
                    }
                    
                    sqlGenreQuery.setLength(sqlGenreQuery.length() - 1);
                    sqlGenreQuery.append(" on conflict do nothing;");
                    
                    jdbcTemplate.update(sqlGenreQuery.toString());
                }
            }
            
            // Заполняем MPA в film
            setMpaToFilm(film);

            // Заполняем genres в film
            setGenresToFilm(film);
            
        } else {
            throw new UserNotFoundException(String
                    .format("Пользователь с id=%s не найден в базе данных.", film.getId()));
        }
    }
    
    @Override
    public Film get(Long id) {
        String sqlQuery = "select " +
                "FILM_ID, " +
                "FILM_NAME, " +
                "DESCRIPTION, " +
                "RELEASE_DATE, " +
                "DURATION, RATE, " +
                "MPA_RATE " +
                "from FILMS " +
                "where FILM_ID = ?";
        
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> filmMapper.makeFilm(rs), id)
                .stream()
                .findAny()
                .orElse(null);
            /*.orElseThrow(() -> {
                throw new FilmNotFoundException(String
                    .format("Фильм с id=%s не найден в базе данных.", id));
            });*/
    }
    
    @Override
    public List<Film> getFilms() {
        String sqlQuery = "select " +
                "FILM_ID, " +
                "FILM_NAME, " +
                "DESCRIPTION, " +
                "RELEASE_DATE, " +
                "DURATION, " +
                "RATE, " +
                "MPA_RATE" +
                " from FILMS f ";
        
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> filmMapper.makeFilm(rs));
    }
    
    @Override
    public List<Film> getTopRatedFilms(int listSize) {
        String sqlQuery = "select " +
                "FILM_ID, " +
                "FILM_NAME, " +
                "DESCRIPTION, " +
                "RELEASE_DATE, " +
                "DURATION, " +
                "RATE, " +
                "MPA_RATE " +
                "from FILMS " +
                "order by RATE desc " +
                "limit ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> filmMapper.makeFilm(rs), listSize);
    }
    
    @Override
    public void addLikeToFilm(Long filmId, Long userId) {
        // Как оказалось, RATE можно было не менять, в тестах все равно не проверяется.
        String sqlQuery = "update FILMS set RATE = (select RATE from FILMS where FILM_ID = ?) + 1 where FILM_ID = ?; " +
                "insert into FILMLIKERS values ( ?, ? )";
        jdbcTemplate.update(sqlQuery, filmId, filmId, filmId, userId);
    }
    
    @Override
    public void removeLikeFromFilm(Long filmId, Long userId) {
        String sqlQuery = "update FILMS set RATE = (select RATE from FILMS where FILM_ID = ?) - 1 where FILM_ID = ?; " +
                "delete from FILMLIKERS where USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, filmId, userId);
    }
    
    
    //--------------------------------------------------------------------------
    
    @Override
    public void filmsClear() {
        jdbcTemplate.execute("delete from FILMLIKERS; " +
                "delete from FILMGENRES; " +
                "delete from FILMS;");
    }

    @Override
    public boolean isFilmExist(Long id) {
        String sqlQuery = "select " +
                "FILM_ID, " +
                "FILM_NAME, " +
                "DESCRIPTION, " +
                "RELEASE_DATE, " +
                "DURATION, " +
                "RATE, " +
                "MPA_RATE " +
                "from FILMS " +
                "where FILM_ID = ?";
        
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> filmMapper.makeFilm(rs), id).stream().findAny().isPresent();
    }
    
    private void setMpaToFilm(Film film) {
        String sqlMpaQuery = "select " +
                "MPA_ID, " +
                "MPA_NAME, " +
                "MPA_DESCRIPTION " +
                "from MPAS " +
                "where MPA_ID = ?";
        film.setMpa(Objects.requireNonNull(
                jdbcTemplate.queryForObject(
                        sqlMpaQuery,
                        (rs, rowNum) -> mpaMapper.makeMpa(rs),
                        film.getMpa().getId())));
    }
    
    private void setGenresToFilm(Film film) {
        String sqlGenresQuery = "select " +
                "g.GENRE_ID, " +
                "GENRE_NAME " +
                "from FILMGENRES f " +
                "join GENRES g on g.GENRE_ID = f.GENRE_ID " +
                "where f.FILM_ID = ? " +
                "order by GENRE_ID";
        film.setGenres(jdbcTemplate.query(sqlGenresQuery, (rs, rowNum) -> genreMapper.makeGenre(rs), film.getId()));
    }
}
