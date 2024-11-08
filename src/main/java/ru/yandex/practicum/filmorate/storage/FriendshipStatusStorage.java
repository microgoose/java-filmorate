package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FriendshipStatus;

import java.util.List;
import java.util.Optional;

public interface FriendshipStatusStorage {
    List<FriendshipStatus> findAll();

    Optional<FriendshipStatus> findById(long id);
}
