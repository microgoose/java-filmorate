package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> findAll();

    Optional<User> findById(long userId);

    List<User> getFriends(long userId);

    List<User> getCommonFriends(long firstUserId, long secondUserId);

    User add(User user);

    User update(User user);

    boolean contains(long userId);
}
