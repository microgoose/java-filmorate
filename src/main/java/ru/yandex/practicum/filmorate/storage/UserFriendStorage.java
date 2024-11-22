package ru.yandex.practicum.filmorate.storage;

public interface UserFriendStorage {
    void add(long userId, long friendId, long statusId);

    void remove(long userId, long friendId);
}
