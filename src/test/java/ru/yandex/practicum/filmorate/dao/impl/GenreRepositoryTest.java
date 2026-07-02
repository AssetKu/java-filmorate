package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreRepositoryTest {
    private final GenreDao genreRepository;
    
    @Test
    @DisplayName("Проверка работы метода getGenre в GenreRepository")
    void getGenre() {
        Genre genre = genreRepository.getGenre(3L);
        
        assertEquals(3L, genre.getId());
        assertEquals("Мультфильм", genre.getName());
    }
    
    @Test
    @DisplayName("Проверка работы метода getAllGenres в GenreRepository")
    void getAllGenres() {
        List<Genre> genres = genreRepository.getAllGenres();
        assertEquals(6, genres.size());
    }
}