package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmRepositoryTest {
    
    private final FilmDao repository;
    
    @Test
    @DisplayName("Проверка работы метода save в FilmRepository")
    void save() {
        Film film = new Film("TestFilm", "TestDescription", LocalDate.of(1995, 8, 15), 140, 18, new Mpa(5L));
        
        repository.save(film);
        Long newId = film.getId();
        
        assertEquals(4L, newId);
    }
    
    @Test
    @DisplayName("Проверка работы метода delete в FilmRepository")
    void delete() {
        repository.delete(2L);
        
        assertEquals(2, repository.getFilms().size());
    }
    
    @Test
    @DisplayName("Проверка работы метода update в FilmRepository")
    void update() {
        Film film = new Film("TestFilm", "TestDescription", LocalDate.of(1995, 8, 15), 140, 18, new Mpa(5L));
        film.setId(2L);
        
        repository.update(film);
        Film testFilm = repository.get(2L);
        
        assertEquals("TestDescription", testFilm.getDescription());
    }
    
    @Test
    @DisplayName("Проверка работы метода get в FilmRepository")
    void get() {
        assertEquals("New film about friends", repository.get(3L).getDescription());
    }
    
    @Test
    @DisplayName("Проверка работы метода getFilms в FilmRepository")
    void getFilms() {
        assertEquals(3, repository.getFilms().size());
        assertEquals("New film about friends", repository.getFilms().get(2).getDescription());
    }
    
    @Test
    @DisplayName("Проверка работы метода getTopRatedFilms в FilmRepository")
    void getTopRatedFilms() {
        assertEquals(3, repository.getTopRatedFilms(5).size());
        assertEquals("New film about friends", repository.getTopRatedFilms(5).get(0).getDescription());
        assertEquals("labore nulla", repository.getTopRatedFilms(5).get(2).getName());
    }
    
    @Test
    @DisplayName("Проверка работы метода addLikeToFilm в FilmRepository")
    void addLikeToFilm() {
        repository.addLikeToFilm(3L, 2L);
        
        assertEquals(9, repository.get(3L).getRate());
    }
    
    @Test
    @DisplayName("Проверка работы метода removeLikeFromFilm в FilmRepository")
    void removeLikeFromFilm() {
        repository.removeLikeFromFilm(3L, 2L);
        
        assertEquals(7, repository.get(3L).getRate());
    }
    
    @Test
    @DisplayName("Проверка работы метода filmsClear в FilmRepository")
    void filmsClear() {
        repository.filmsClear();
        
        assertEquals(0, repository.getFilms().size());
    }
    
    @Test
    @DisplayName("Проверка работы метода isFilmExist в FilmRepository")
    void isFilmExist() {
        assertTrue(repository.isFilmExist(3L));
        assertFalse(repository.isFilmExist(8L));
    }
}