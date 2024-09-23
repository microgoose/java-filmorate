package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/films")
@Slf4j
public class FilmController {
    int id = 1;
    Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getAll() {
        log.info("Get all films");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("Add film: {}", film);
        validate(film);
        film.setId(id++);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Update film: {}", film);
        validate(film);

        if (film.getId() == null)
            throw new IllegalArgumentException("Не передан id!");
        if (!films.containsKey(film.getId()))
            throw new IllegalArgumentException("Не найден фильм с таким id!");

        films.put(film.getId(), film);
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
