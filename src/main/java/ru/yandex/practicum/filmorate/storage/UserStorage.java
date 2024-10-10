package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAll();

    User get(long id);

    User add(User user);

    User update(User user);

    boolean contains(long userId);
}
