package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaRepositoryTest {
    private final MpaDao repository;
    
    @Test
    @DisplayName("Проверка работы метода get в MpaRepository")
    void get() {
        Mpa mpa = repository.get(3L);
        
        assertEquals(3L, mpa.getId());
        assertEquals("PG-13", mpa.getName());
        assertEquals("детям до 13 лет просмотр не желателен", mpa.getDescription());
    }
    
    @Test
    @DisplayName("Проверка работы метода getAllMpas в MpaRepository")
    void getAllMpas() {
        List<Mpa> mpa = repository.getAllMpas();
        
        assertEquals(5, mpa.size());
    }
}