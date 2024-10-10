package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping(path = "/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping(path = "/{filmId}")
    public Film getFilm(@PathVariable long filmId) {
        log.info("Get film {}", filmId);
        return filmService.getFilm(filmId);
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("Get all films");
        return filmService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@RequestBody Film film) {
        log.info("Add film: {}", film);
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Update film: {}", film);
        return filmService.updateFilm(film);
    }

    @PutMapping(path = "/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Film addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("Add like to film {} from user {}", filmId, userId);
        return filmService.addLike(userId, filmId);
    }

    @DeleteMapping(path = "/{filmId}/like/{userId}")
    public Film removeLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("Remove like from film {} with user {}", filmId, userId);
        return filmService.removeLike(userId, filmId);
    }

    @GetMapping(path = "/popular")
    public List<Film> getMostPopular(@RequestParam(required = false, defaultValue = "10") Integer count) {
        log.info("Get most popular {} films", count);
        return filmService.getMosPopular(count);
    }
}
