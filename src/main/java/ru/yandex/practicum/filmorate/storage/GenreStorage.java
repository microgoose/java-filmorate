package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    List<Genre> findAll();

    Optional<Genre> findById(long genreId);

    List<Genre> findById(List<Long> genresId);

    List<Genre> findByFilm(long filmId);
}
