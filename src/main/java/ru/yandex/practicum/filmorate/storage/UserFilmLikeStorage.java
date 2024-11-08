package ru.yandex.practicum.filmorate.storage;

public interface UserFilmLikeStorage {
    void addLike(long userId, long filmId);

    void removeLike(long userId, long filmId);

    boolean contains(long userId, long filmId);
}
