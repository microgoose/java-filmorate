package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/{userId}")
    public User getFilm(@PathVariable Long userId) {
        log.info("Get user {}", userId);
        return userService.getUser(userId);
    }

    @GetMapping
    public List<User> getAll() {
        log.info("Get all users");
        return userService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User add(@RequestBody User user) {
        log.info("Add user: {}", user);
        return userService.addUser(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("Update user: {}", user);
        return userService.updateUser(user);
    }

    @PostMapping(path = "/{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Add user {} friend {}", userId, friendId);
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping(path = "/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Remove user {} friend {}", userId, friendId);
        userService.removeFriend(userId, friendId);
    }

    @GetMapping(path = "/{userId}/friends")
    public List<User> getFriends(@PathVariable Long userId) {
        log.info("Get user {} friends", userId);
        return userService.getFriends(userId);
    }

    @GetMapping(path = "/{userId}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long userId, @PathVariable Long otherId) {
        log.info("Get users {},{} common friends", userId, otherId);
        return userService.getCommonFriends(userId, otherId);
    }
}
