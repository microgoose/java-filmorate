package ru.yandex.practicum.filmorate.exceptions;

public class FilmNotFoundException extends NotFoundException {
    public FilmNotFoundException(Long filmId) {
        super(String.format("Фильм с ID: %d не найден!", filmId));
    }
}
