package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FriendshipDao;
import ru.yandex.practicum.filmorate.dao.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FriendshipRepository implements FriendshipDao {
    
    private final UserMapper userMapper;
    private final JdbcTemplate jdbcTemplate;
    
    @Override
    public boolean friending(Long userId, Long friendId) {
        String sqlQuery = "insert into FRIENDSHIP values ( ?, ? )";
        
        return jdbcTemplate.update(sqlQuery, userId, friendId) == 1;
    }
    
    @Override
    public boolean unfriending(Long userId, Long friendId) {
        String sqlQuery = "delete from FRIENDSHIP where USER_ID = ? and FRIEND_ID = ?";
        
        return jdbcTemplate.update(sqlQuery, userId, friendId) == 1;
    }
    
    @Override
    public List<User> getFriends(Long userId) {
        String sqlQuery = "select USER_ID, USER_EMAIL, USER_LOGIN, USER_NAME, BIRTHDAY from USERS " +
            "where user_id in (select FRIEND_ID from FRIENDSHIP where user_id = ?)";
        
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> userMapper.makeUser(rs), userId);
    }
    
    @Override
    public List<User> getCommonFriends(Long firstId, Long secondId) {
        
        String sqlQuery = "select USER_ID, USER_EMAIL, USER_LOGIN, USER_NAME, BIRTHDAY from USERS " +
            "where USER_ID in (select FRIEND_ID from FRIENDSHIP where USER_ID = ?) and " +
            "      USER_ID in (select FRIEND_ID from FRIENDSHIP where USER_ID = ?)";
        
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> userMapper.makeUser(rs), firstId, secondId);
    }
}
