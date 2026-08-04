package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.util.List;

@Component("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> mapper = (rs, rowNum) -> {
        User user = new User();

        user.setId(rs.getInt("USER_ID"));
        user.setEmail(rs.getString("USER_EMAIL"));
        user.setLogin(rs.getString("USER_LOGIN"));
        user.setName(rs.getString("USER_NAME"));
        user.setBirthday(rs.getDate("BIRTHDAY").toLocalDate());

        return user;
    };

    @Override
    public User create(User user) {

        String sql = """
                INSERT INTO USERS
                (USER_EMAIL, USER_LOGIN, USER_NAME, BIRTHDAY)
                VALUES (?, ?, ?, ?)
                """;

        jdbcTemplate.update(
                sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday())
        );

        Integer id = jdbcTemplate.queryForObject(
                "SELECT MAX(USER_ID) FROM USERS",
                Integer.class
        );

        user.setId(id);

        return user;
    }

    @Override
    public User update(User user) {

        String sql = """
                UPDATE USERS
                SET USER_EMAIL = ?,
                    USER_LOGIN = ?,
                    USER_NAME = ?,
                    BIRTHDAY = ?
                WHERE USER_ID = ?
                """;

        jdbcTemplate.update(
                sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId()
        );

        return user;
    }

    @Override
    public User getById(int id) {

        List<User> users = jdbcTemplate.query(
                """
                        SELECT *
                        FROM USERS
                        WHERE USER_ID = ?
                        """,
                mapper,
                id
        );

        return users.isEmpty()
                ? null
                : users.get(0);
    }

    @Override
    public List<User> getAll() {

        return jdbcTemplate.query(
                "SELECT * FROM USERS",
                mapper
        );
    }

    public void addFriend(int userId, int friendId) {

        jdbcTemplate.update(
                """
                INSERT INTO FRIENDSHIP (USER_ID, FRIEND_ID)
                VALUES (?, ?)
                """,
                userId,
                friendId
        );
    }

    public void removeFriend(int userId, int friendId) {

        jdbcTemplate.update(
                """
                DELETE FROM FRIENDSHIP
                WHERE USER_ID = ?
                  AND FRIEND_ID = ?
                """,
                userId,
                friendId
        );
    }

    public List<User> getFriends(int userId) {

        return jdbcTemplate.query(
                """
                SELECT u.*
                FROM USERS u
                JOIN FRIENDSHIP f
                  ON u.USER_ID = f.FRIEND_ID
                WHERE f.USER_ID = ?
                """,
                mapper,
                userId
        );
    }

    public List<User> getCommonFriends(int userId,
                                       int otherId) {

        return jdbcTemplate.query(
                """
                SELECT u.*
                FROM USERS u
                WHERE u.USER_ID IN (
                    SELECT FRIEND_ID
                    FROM FRIENDSHIP
                    WHERE USER_ID = ?
    
                    INTERSECT
    
                    SELECT FRIEND_ID
                    FROM FRIENDSHIP
                    WHERE USER_ID = ?
                )
                """,
                mapper,
                userId,
                otherId
        );
    }
}