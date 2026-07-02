package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDao {
    
    void save(Film film);
    
    void delete(Long id);
    
    void update(Film film);
    
    Film get(Long id);
    
    List<Film> getFilms();
    
    List<Film> getTopRatedFilms(int listSize);
    
    void addLikeToFilm(Long filmId, Long userId);
    
    void removeLikeFromFilm(Long filmId, Long userId);
    
    //++++++++++++++++
    void filmsClear();
    
    boolean isFilmExist(Long id);
}
