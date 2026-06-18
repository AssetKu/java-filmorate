package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserControllerTest {

    private UserController controller;

    @BeforeEach
    void setUp() {
        controller = new UserController();
    }

    @Test
    void shouldThrowIfEmailInvalid() {
        User user = new User();
        user.setEmail("wrongEmail");
        user.setLogin("login");

        Assertions.assertThrows(ValidationException.class,
                () -> controller.create(user));
    }

    @Test
    void shouldThrowIfLoginHasSpaces() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setLogin("bad login");

        Assertions.assertThrows(ValidationException.class,
                () -> controller.create(user));
    }

    @Test
    void shouldThrowIfBirthdayInFuture() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setLogin("login");
        user.setBirthday(LocalDate.now().plusDays(1));

        Assertions.assertThrows(ValidationException.class,
                () -> controller.create(user));
    }

    @Test
    void shouldSetNameIfEmpty() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setLogin("login");

        User created = controller.create(user);

        Assertions.assertEquals("login", created.getName());
    }

    @Test
    void shouldCreateValidUser() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setLogin("login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        User created = controller.create(user);

        Assertions.assertEquals(1, created.getId());
        Assertions.assertEquals("Name", created.getName());
    }
}