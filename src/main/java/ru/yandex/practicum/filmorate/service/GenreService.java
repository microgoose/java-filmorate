package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {
    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public List<Genre> findAll() {
        return genreStorage.findAll();
    }

    public Genre findById(long genreId) {
        Optional<Genre> genre = genreStorage.findById(genreId);
        return genre.orElseThrow(() -> new GenreNotFoundException(genreId));
    }

    public List<Genre> findById(List<Long> genresId) {
        return genreStorage.findById(genresId);
    }

    public List<Genre> findByFilm(long filmId) {
        return genreStorage.findByFilm(filmId);
    }
}
