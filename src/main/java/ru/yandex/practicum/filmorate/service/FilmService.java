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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final UserFilmLikeStorage filmLikesStorage;
    private final FilmGenreService filmGenreService;
    private final MpaRatingService mpaRatingService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage,
                       UserFilmLikeStorage filmLikesStorage, FilmGenreService filmGenreService, MpaRatingService mpaRatingService) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.filmLikesStorage = filmLikesStorage;
        this.filmGenreService = filmGenreService;
        this.mpaRatingService = mpaRatingService;
    }

    public Film getFilm(Long filmId) {
        if (filmId == null) {
            throw new IllegalArgumentException("Отсутствует id фильма!");
        }

        Optional<Film> film = filmStorage.get(filmId);

        if (film.isEmpty()) {
            throw new FilmNotFoundException(filmId);
        }

        return film.get();
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film addFilm(Film film) {
        validateFilm(film);

        if (film.getRate() == null) {
            film.setRate(0);
        }

        if (film.getGenres() == null) {
            film.setGenres(new ArrayList<>());
        } else {
            film.setGenres(new HashSet<>(film.getGenres()).stream().toList());
        }

        if (film.getMpa() != null && !mpaRatingService.contains(film.getMpa().getId())) {
            throw new ValidationException("Не существует МПА рейтинг с ID: " + film.getMpa().getId());
        }

        filmStorage.add(film);

        if (!film.getGenres().isEmpty()) {
            filmGenreService.add(film);
        }

        return film;
    }

    public Film updateFilm(Film film) {
        validateFilm(film);
        validateFilmIdExistence(film.getId());
        filmStorage.update(film);

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            filmGenreService.update(film);
        }

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

        return filmStorage.getMostPopular(count);
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
            if (filmLikesStorage.contains(userId, filmId)) {
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
        if (film.getDuration() < 0) {
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
