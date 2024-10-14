package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserFilmLikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final UserFilmLikeStorage filmLikesStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, UserFilmLikeStorage filmLikesStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.filmLikesStorage = filmLikesStorage;
    }

    public Film getFilm(Long filmId) {
        if (filmId == null) {
            throw new IllegalArgumentException("Отсутствует id фильма!");
        }

        Film film = filmStorage.get(filmId);

        if (film == null) {
            throw new FilmNotFoundException(filmId);
        }

        return film;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film addFilm(Film film) {
        validateFilm(film);

        if (film.getRate() == null) {
            film.setRate(0);
        }

        filmStorage.add(film);
        return film;
    }

    public Film updateFilm(Film film) {
        validateFilm(film);
        validateFilmIdExistence(film.getId());
        filmStorage.update(film);
        return film;
    }

    public Film addLike(Long userId, Long filmId) {
        return changeLike(userId, filmId, true);
    }

    public Film removeLike(Long userId, Long filmId) {
        return changeLike(userId, filmId, false);
    }

    public List<Film> getMosPopular(int count) {
        if (count < 0) {
            throw new InvalidParameterException("Количество фильмов должно быть больше нуля!");
        }

        List<Film> films = getAll();

        if (films.isEmpty()) {
            return films;
        }

        films.sort((f1, f2) -> f2.getRate().compareTo(f1.getRate()));
        return films.stream().limit(Math.min(films.size(), count)).collect(Collectors.toList());
    }

    private Film changeLike(Long userId, Long filmId, boolean isAdd) {
        if (userId == null) {
            throw new IllegalArgumentException("Отсутствует id пользователя!");
        }
        if (!userStorage.contains(userId)) {
            throw new UserNotFoundException(userId);
        }

        Film film = getFilm(filmId);

        if (isAdd) {
            if (filmLikesStorage.containsLike(userId, filmId)) {
                throw new IllegalArgumentException("Лайк уже был поставлен");
            }

            filmLikesStorage.addLike(userId, filmId);
            film.setRate(film.getRate() + 1);
        } else {
            filmLikesStorage.removeLike(userId, filmId);
            film.setRate(film.getRate() - 1);
        }

        updateFilm(film);

        return film;
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Название фильма не может быть пустым!");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание должно быть не более 200 символов!");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Фильм не может быть выпушен ранее чем 28 декабря 1895 года!");
        }
        if (film.getDuration().isNegative()) {
            throw new ValidationException("Продолжительнсть фильма не может быть отрицательной!");
        }
    }

    private void validateFilmIdExistence(Long filmId) {
        if (filmId == null) {
            throw new IllegalArgumentException("Отсутствует id фильма!");
        }
        if (!filmStorage.contains(filmId)) {
            throw new FilmNotFoundException(filmId);
        }
    }
}
