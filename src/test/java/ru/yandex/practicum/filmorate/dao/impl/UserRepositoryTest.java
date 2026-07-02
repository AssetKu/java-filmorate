package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserRepositoryTest {
    private final UserDao repository;
    
    @Test
    @DisplayName("Проверка работы метода save в UserRepository")
    void save() {
        User user = new User(8L, "somemail@mail.com", "login", "SomeName", LocalDate.of(1985, 12, 25));
        
        Long newId = repository.save(user);
        assertEquals(4L, newId);
        
        User newUser = repository.get(4L);
        assertEquals("somemail@mail.com", newUser.getEmail());
    }
    
    @Test
    @DisplayName("Проверка работы метода get в UserRepository")
    void get() {
        User user = repository.get(2L);
        
        assertEquals("friend", user.getName());
    }
    
    @Test
    @DisplayName("Проверка работы метода update в UserRepository")
    void update() {
        User user = new User(2L, "somemail@mail.com", "login", "SomeName", LocalDate.of(1985, 12, 25));
        
        repository.update(user);
        
        User newUser = repository.get(2L);
        assertEquals("somemail@mail.com", newUser.getEmail());
    }
    
    @Test
    @DisplayName("Проверка работы метода delete в UserRepository")
    void delete() {
        repository.delete(2L);
        
        assertThrows(UserNotFoundException.class, () -> repository.get(2L));
    }
    
    @Test
    @DisplayName("Проверка работы метода getUsers в UserRepository")
    void getUsers() {
        List<User> users = repository.getUsers();
        
        assertEquals(3, users.size());
        assertEquals("dolore", users.get(0).getName());
    }
    
    @Test
    @DisplayName("Проверка работы метода usersClear в UserRepository")
    void usersClear() {
        repository.usersClear();
        
        assertEquals(0, repository.getUsers().size());
    }
    
    @Test
    @DisplayName("Проверка работы метода isUserExist в UserRepository")
    void isUserExist() {
        assertTrue(repository.isUserExist(1L));
        assertTrue(repository.isUserExist(2L));
        assertTrue(repository.isUserExist(3L));
    }
}