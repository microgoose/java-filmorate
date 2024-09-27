package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class FilmService {
    private final FilmRepository filmRepository = new FilmRepository();

    public List<Film> getAll() {
        return filmRepository.getAll();
    }

    public Film addFilm(Film film) {
        validate(film);
        filmRepository.add(film);
        return film;
    }

    public Film updateFilm(Film film) {
        if (film.getId() == null)
            throw new IllegalArgumentException("Не передан id!");
        if (!filmRepository.contains(film))
            throw new IllegalArgumentException("Не найден фильм с таким id!");

        validate(film);
        filmRepository.update(film.getId(), film);
        return film;
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
