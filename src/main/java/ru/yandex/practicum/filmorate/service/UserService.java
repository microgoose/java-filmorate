package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFriend;
import ru.yandex.practicum.filmorate.storage.UserFriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        if (userId == null)
            throw new IllegalArgumentException("Отсутствует id пользователя!");

        User user = userStorage.get(userId);

        if (user == null)
            throw new UserNotFoundException(userId);

        return userStorage.get(userId);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isEmpty())
            user.setName(user.getLogin());

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
        if (!userStorage.contains(userId))
            throw new UserNotFoundException(userId);

        return friendStorage.getFriends(userId).stream()
                .map(userFriend -> userStorage.get(userFriend.getFriendId()))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long firstUser, Long secondUser) {
        validateUserIdExistence(firstUser, secondUser);
        List<UserFriend> firstFriends = friendStorage.getFriends(firstUser);
        List<UserFriend> secondFriends = friendStorage.getFriends(secondUser);

        return firstFriends.stream()
                .filter(ff -> secondFriends.stream().anyMatch(sf -> Objects.equals(ff.getFriendId(), sf.getFriendId())))
                .map(ff -> userStorage.get(ff.getFriendId()))
                .collect(Collectors.toList());
    }

    public void addFriend(Long userId, Long friendId) {
        validateUserIdExistence(userId, friendId);

        if (friendStorage.containsFriend(userId, friendId))
            throw new ValidationException("У пользователя уже есть такой друг!");

        friendStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        validateUserIdExistence(userId, friendId);

        if (!friendStorage.containsFriend(userId, friendId))
            throw new ValidationException("У пользователя нет такого друга!");

        friendStorage.removeFriend(userId, friendId);
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@"))
            throw new ValidationException("Неккоректный email.");
        if (user.getLogin() == null || user.getLogin().trim().isEmpty())
            throw new ValidationException("Логин не может быть пустым.");
        if (user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("Дата рождения не может быть больше текущей.");
    }

    private void validateUserIdExistence(Long ...userIds) {
        for (Long userId : userIds) {
            if (userId == null)
                throw new IllegalArgumentException("Отсутствует id пользователя!");
            if (!userStorage.contains(userId))
                throw new UserNotFoundException(userId);
        }
    }
}
