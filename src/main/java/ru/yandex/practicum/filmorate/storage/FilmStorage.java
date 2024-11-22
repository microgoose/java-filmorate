package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> getAll();

    Optional<Film> get(long id);

    Film add(Film film);

    Film update(Film film);

    List<Film> getMostPopular(int count);

    boolean contains(long id);
}
