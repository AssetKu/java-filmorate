package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.dao.mapper.UserMapper;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
@Primary
@RequiredArgsConstructor
@Slf4j
public class UserRepository implements UserDao {
    
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;
    
    @Override
    public Long save(User user) {
        String sqlQuery = "insert into USERS (" +
            "USER_EMAIL, " +
            "USER_LOGIN, " +
            "USER_NAME, " +
            "BIRTHDAY) " +
            "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
    
    /**
     * Возвращает пользователя из базы данных по id
     *
     * @param id - id пользователя, которого нужно вернуть
     * @return - найденный пользователь или выброшенное исключение {@link UserNotFoundException}
     */
    @Override
    public User get(Long id) {
        final String sqlQuery = "select " +
            "USER_ID, " +
            "USER_EMAIL, " +
            "USER_LOGIN, " +
            "USER_NAME, " +
            "BIRTHDAY" +
            " from USERS " +
            "where USER_ID = ?";
        
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> userMapper.makeUser(rs), id)
            .stream()
            .findAny()
            .orElseThrow(() -> {
                throw new UserNotFoundException(String
                    .format("Пользователь с id=%s не найден в базе данных.", id));
            });
    }
    
    @Override
    public void update(User user) {
        String sqlQuery = "update USERS set " +
            "USER_NAME = ?, " +
            "USER_LOGIN = ?, " +
            "USER_EMAIL = ?, " +
            "BIRTHDAY = ? " +
            "where USER_ID = ?";
        
        if (isUserExist(user.getId())) {
            jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());
        } else {
            throw new UserNotFoundException(String
                .format("Пользователь с id=%s не найден в базе данных.", user.getId()));
        }
    }
    
    @Override
    public void delete(Long id) {
        final String sqlQuery = String.format("delete from USERS where USER_ID = %s", id);
        
        if (isUserExist(id)) {
            jdbcTemplate.execute(sqlQuery);
        } else {
            throw new UserNotFoundException(String
                .format("Пользователь с id=%s не найден в базе данных.", id));
        }
    }
    
    @Override
    public List<User> getUsers() {
        final String sqlQuery = "select " +
            "USER_ID, " +
            "USER_EMAIL, " +
            "USER_LOGIN, " +
            "USER_NAME, " +
            "BIRTHDAY " +
            "from USERS";
        
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> userMapper.makeUser(rs));
    }
    
    @Override
    public void usersClear() {
        jdbcTemplate.execute("delete from USERS");
    }
    
    @Override
    public boolean isUserExist(Long id) {
        final String sqlQuery = "select * from USERS where USER_ID = ?";
        
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> userMapper.makeUser(rs), id).stream().findAny().isPresent();
    }
}
