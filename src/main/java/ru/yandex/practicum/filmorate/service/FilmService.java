package ru.yandex.practicum.filmorate.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    
    @NonNull
    private FilmDao filmDao;
    @NonNull
    private UserDao userDao;
    
    /**
     * Добавляет фильм, полученный от {@link ru.yandex.practicum.filmorate.controller.FilmController} в {@link FilmDao}
     * Перед добавлением назначает фильму id
     *
     * @param film - объект класса Film, который нужно добавить
     */
    public void addFilm(Film film) {
        filmDao.save(film);
    }
    
    /**
     * Обновляет данные
     *
     * @param film - объект класса Film, который нужно добавить
     */
    public void updateFilm(Film film) {
        filmDao.update(film);
    }
    
    /**
     * Удаляет фильм из FilmStorage по id
     *
     * @param id - id фильма, полученный от FilmController
     */
    public void deleteFilm(Long id) {
        filmDao.delete(id);
    }
    
    /**
     * Возвращает фильм по id
     *
     * @param id - id, полученный из FilmController
     * @return - возвращает фильм из хранилища
     */
    public Film getFilm(Long id) {
        Film result = filmDao.get(id);
        if (result != null) {
            return result;
        } else {
            throw new FilmNotFoundException(String
                    .format("Фильм с id=%s не найден в базе данных.", id));
        }
    }
    
    /**
     * Возвращает весь список фильмов из хранилища
     *
     * @return - возвращает список фильмов
     */
    public List<Film> getFilms() {
        return filmDao.getFilms();
    }
    
    /**
     * Добавляет лайк к фильму
     *
     * @param filmId - id фильма, к которому нужно добавить лайк
     * @param userId - id пользователя, который добавляет лайк
     */
    public void addLikeToFilm(Long filmId, Long userId) {
        isFilmAndUserExist(filmId, userId);
        
        filmDao.addLikeToFilm(filmId, userId);
    }
    
    /**
     * Удаляет лайк у фильма
     *
     * @param filmId - id фильма
     * @param userId - id пользователя, который до этого ставил этот лайк
     */
    public void removeLikeFromFilm(Long filmId, Long userId) {
        isFilmAndUserExist(filmId, userId);
        
        filmDao.removeLikeFromFilm(filmId, userId);
    }
    
    /**
     * Возвращает список самых рейтинговых фильмов
     *
     * @param listSize размер списка возвращаемых фильмов
     * @return - возвращает список фильмов
     */
    public List<Film> getTopRatedFilms(int listSize) {
        return filmDao.getTopRatedFilms(listSize);
    }
    
    
    //++++++++++++++++
    private void isFilmAndUserExist(Long filmId, Long userId) {
        if (!filmDao.isFilmExist(filmId)) {
            throw new FilmNotFoundException(String
                .format("Фильм с id=%s не найден в базе.", filmId));
        }
        if (!userDao.isUserExist(userId)) {
            throw new UserNotFoundException(String
                .format("Пользователь с id=%s не найден в базе.", userId));
        }
    }
    
    /**
     * Очищает хранилище фильмов и сбрасывает счетчик id
     */
    public void filmsClear() {
        filmDao.filmsClear();
    }
}
