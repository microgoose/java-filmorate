package ru.yandex.practicum.filmorate.exceptions;

public class MpaRatingNotFoundException extends NotFoundException {
    public MpaRatingNotFoundException(Long id) {
        super(String.format("MPA рейтинг с ID: %d не найден!", id));
    }
}
