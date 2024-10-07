package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFriend;

import java.util.List;

public interface UserFriendStorage {
    List<UserFriend> getFriends(User user);

    UserFriend addFriend(User user, User friend);

    void removeFriend(User user, User friend);
}
