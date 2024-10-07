package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isEmpty())
            user.setName(user.getLogin());

        validate(user);
        userStorage.add(user);
        return user;
    }

    public User updateUser(User user) {
        if (user.getId() == null)
            throw new IllegalArgumentException("Не передан id!");
        if (!userStorage.contains(user))
            throw new IllegalArgumentException("Не найден пользователь с таким id!");

        validate(user);
        userStorage.update(user);
        return user;
    }

    public void addFriend(User user, User friend) {
        friendStorage.addFriend(user, friend);
    }

    public void removeFriend(User user, User friend) {
        friendStorage.removeFriend(user, friend);
    }

    public List<User> getCommonFriends(User firstUser, User secondUser) {
        List<UserFriend> firstFriends = friendStorage.getFriends(firstUser);
        List<UserFriend> secondFriends = friendStorage.getFriends(secondUser);

        return firstFriends.stream()
                .filter(ff -> secondFriends.stream().anyMatch(sf -> Objects.equals(ff.getFriendId(), sf.getFriendId())))
                .map(ff -> userStorage.get(ff.getUserId()))
                .collect(Collectors.toList());
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
