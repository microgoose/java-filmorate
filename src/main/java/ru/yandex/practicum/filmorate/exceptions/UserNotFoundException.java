package ru.yandex.practicum.filmorate.exceptions;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(Long userId) {
        super(String.format("Пользователь с ID: %d не найден!", userId));
    }
}
