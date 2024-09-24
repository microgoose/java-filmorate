package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    int id = 1;
    Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> getAll() {
        log.info("Get all users");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public ResponseEntity<User> add(@RequestBody User user) {
        log.info("Add user: {}", user);

        try {
            validate(user);
        } catch (ValidationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(user);
        }

        user.setId(id++);

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<User> update(@RequestBody User user) {
        log.info("Update user: {}", user);

        try {
            if (user.getId() == null)
                throw new IllegalArgumentException("Не передан id!");
            if (!users.containsKey(user.getId()))
                throw new IllegalArgumentException("Не найден пользователь с таким id!");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(user);
        }

        try {
            validate(user);
        } catch (ValidationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(user);
        }

        users.put(user.getId(), user);
        return ResponseEntity.ok(user);
    }

    private void validate(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@"))
            throw new ValidationException("Неккоректный email!");
        if (user.getLogin() == null || user.getLogin().trim().isEmpty())
            throw new ValidationException("Логин не может быть пустым!");
        if (user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("Дата рождения не может быть больше текущей!");
    }
}
