package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAll();

    Film get(long id);

    Film add(Film film);

    Film update(Film film);

    boolean contains(long id);
}
