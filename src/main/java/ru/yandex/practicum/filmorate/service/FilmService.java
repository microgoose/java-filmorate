package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserFilmLikeStorage;

import java.time.LocalDate;
import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserFilmLikeStorage filmLikesStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserFilmLikeStorage filmLikesStorage) {
        this.filmStorage = filmStorage;
        this.filmLikesStorage = filmLikesStorage;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film addFilm(Film film) {
        validate(film);
        filmStorage.add(film);
        return film;
    }

    public Film updateFilm(Film film) {
        if (film.getId() == null)
            throw new IllegalArgumentException("Не передан id!");
        if (!filmStorage.contains(film))
            throw new IllegalArgumentException("Не найден фильм с таким id!");

        validate(film);
        filmStorage.update(film);
        return film;
    }

    public Film addLike(User user, Film film) {
        filmLikesStorage.addLike(user, film);
        film.setRate(film.getRate() + 1);
        updateFilm(film);
        return film;
    }

    public Film removeLike(User user, Film film) {
        filmLikesStorage.removeLike(user, film);
        film.setRate(film.getRate() - 1);
        updateFilm(film);
        return film;
    }

    public List<Film> getMosPopular(int size) {
        getAll().sort((f1, f2) -> f2.getRate().compareTo(f1.getRate()));
        return filmStorage.getAll().subList(0, size);
    }

    private void validate(Film film) {
        if (film.getName() == null || film.getName().isEmpty())
            throw new ValidationException("Название фильма не может быть пустым!");
        if (film.getDescription().length() > 200)
            throw new ValidationException("Описание должно быть не более 200 символов!");
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
            throw new ValidationException("Фильм не может быть выпушен ранее чем 28 декабря 1895 года!");
        if (film.getDuration().isNegative())
            throw new ValidationException("Продолжительнсть фильма не может быть отрицательной!");
    }
}
