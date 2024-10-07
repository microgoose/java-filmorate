package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.UserFriend;

import java.util.List;

public interface UserFriendStorage {
    List<UserFriend> getFriends(Long user);

    UserFriend addFriend(Long user, Long friend);

    void removeFriend(Long user, Long friend);

    boolean containsFriend(Long user, Long friend);
}
