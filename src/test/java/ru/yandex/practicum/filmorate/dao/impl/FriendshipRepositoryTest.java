package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.FriendshipDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FriendshipRepositoryTest {
    private final FriendshipDao repository;
    
    @Test
    @DisplayName("Проверка работы метода friending в FriendshipRepository")
    void friending() {
        repository.friending(3L, 2L);
        
        List<User> users = repository.getFriends(3L);
        
        assertEquals(2, users.size());
    }
    
    @Test
    @DisplayName("Проверка работы метода unfriending в FriendshipRepository")
    void unfriending() {
        repository.unfriending(1L, 3L);
        
        List<User> users = repository.getFriends(1L);
        
        assertEquals(1, users.size());
    }
    
    @Test
    @DisplayName("Проверка работы метода getFriends в FriendshipRepository")
    void getFriends() {
        List<User> friends = repository.getFriends(1L);
        
        assertEquals(2, friends.size());
    }
    
    @Test
    @DisplayName("Проверка работы метода getCommonFriends в FriendshipRepository")
    void getCommonFriends() {
        List<User> commonFriends = repository.getCommonFriends(1L, 2L);
        
        assertEquals(1, commonFriends.size());
        assertEquals(3L, commonFriends.get(0).getId());
    }
}