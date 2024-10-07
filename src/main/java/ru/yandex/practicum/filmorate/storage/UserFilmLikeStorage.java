package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFilmLike;

import java.util.List;

public interface UserFilmLikeStorage {
    List<UserFilmLike> getAll();

    List<UserFilmLike> getLikes(User user);

    List<UserFilmLike> getLikes(Film film);

    UserFilmLike addLike(User user, Film film);

    void removeLike(User user, Film film);
}
