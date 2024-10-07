package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.UserFilmLike;

import java.util.List;

public interface UserFilmLikeStorage {
    List<UserFilmLike> getAll();

    List<UserFilmLike> getLikesByUserId(long userId);

    List<UserFilmLike> getLikesByFilmId(long filmId);

    UserFilmLike addLike(long userId, long filmId);

    void removeLike(long userId, long filmId);

    boolean containsLike(long userId, long filmId);
}
