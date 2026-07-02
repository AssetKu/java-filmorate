package ru.yandex.practicum.filmorate.controller;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.annotation.OnCreate;
import ru.yandex.practicum.filmorate.annotation.OnUpdate;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@Validated
@NoArgsConstructor
public class UserController {
    
    private UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping()
    public List<User> getUsers() {
        log.info("Возвращен список пользователей");
        return userService.getUsers();
    }
    
    @PostMapping()
    @Validated(OnCreate.class)
    public User createUser(@RequestBody @Valid User user) {
        nullUserValidationCheck(user);
        nameCorrection(user);
        
        userService.createUser(user);
        log.info(String.format("Добавлен пользователь %s с id=%s.", user.getName(), user.getId()));
        return user;
    }
    
    @PutMapping()
    @Validated(OnUpdate.class)
    public User updateUser(@RequestBody @Valid User user) {
        nameCorrection(user);
        
        userService.updateUser(user);
        log.info(String.format("Данные пользователя с id=%s обновлены.", user.getId()));
        return user;
    }
    
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }
    
    // Возвращает общих друзей двух пользователей
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getCommonFriends(id, otherId);
    }
    
    // Добавляет пользователя friendId в друзья к id и наоборот
    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.friending(id, friendId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.unfriending(id, friendId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    // Возвращает список друзей пользователя
    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        return userService.getFriends(id);
    }
    
    //+++++++++++++++
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearUserMap() {
        log.info("Список пользователей очищен.");
        userService.usersClear();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    private void nullUserValidationCheck(User user) {
        if (user == null) {
            log.warn("Полученный объект user является null.");
            throw new ValidationException("Полученный объект user является null.");
        }
    }
    
    private void nameCorrection(User user) {
        nullUserValidationCheck(user);
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
