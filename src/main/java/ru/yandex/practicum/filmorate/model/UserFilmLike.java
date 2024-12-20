package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserFilmLike {
    public User user;
    public Film film;
}
