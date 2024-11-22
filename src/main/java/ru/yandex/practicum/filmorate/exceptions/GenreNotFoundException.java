package ru.yandex.practicum.filmorate.exceptions;

public class GenreNotFoundException extends NotFoundException {
    public GenreNotFoundException(Long id) {
        super(String.format("Жанр с ID: %d не найден!", id));
    }
}
