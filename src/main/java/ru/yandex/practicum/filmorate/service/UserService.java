package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserFriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.SQLExceptionUtil;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final UserFriendStorage friendStorage;

    @Autowired
    public UserService(UserStorage userStorage, UserFriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public User getUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Отсутствует id пользователя!");
        }

        Optional<User> userOptional = userStorage.findById(userId);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException(userId);
        }

        return userOptional.get();
    }

    public List<User> getAll() {
        return userStorage.findAll();
    }

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        validateUser(user);
        userStorage.add(user);
        return user;
    }

    public User updateUser(User user) {
        validateUserIdExistence(user.getId());
        validateUser(user);
        userStorage.update(user);
        return user;
    }

    public List<User> getFriends(Long userId) {
        if (!userStorage.contains(userId)) {
            throw new UserNotFoundException(userId);
        }

        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Long firstUser, Long secondUser) {
        validateUserIdExistence(firstUser);
        validateUserIdExistence(secondUser);
        return userStorage.getCommonFriends(firstUser, secondUser);
    }

    public void addFriend(Long userId, Long friendId) {
        validateUserIdExistence(userId);
        validateUserIdExistence(friendId);

        try {
            friendStorage.add(userId, friendId, 1L); //pending
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause() instanceof SQLException) {
                if (SQLExceptionUtil.isConstraintViolation((SQLException) ex.getCause())) {
                    throw new ValidationException("Друг уже существует");
                }
            }

            throw ex;
        }
    }

    public void removeFriend(Long userId, Long friendId) {
        validateUserIdExistence(userId);
        validateUserIdExistence(friendId);
        friendStorage.remove(userId, friendId);
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Неккоректный email.");
        }
        if (user.getLogin() == null || user.getLogin().trim().isEmpty()) {
            throw new ValidationException("Логин не может быть пустым.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть больше текущей.");
        }
    }

    private void validateUserIdExistence(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Отсутствует id пользователя!");
        }
        if (!userStorage.contains(userId)) {
            throw new UserNotFoundException(userId);
        }
    }
}
