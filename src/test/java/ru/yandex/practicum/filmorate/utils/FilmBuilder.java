package ru.yandex.practicum.filmorate.utils;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.time.LocalDate;

public class FilmBuilder {
    public static Film createFilm() {
        return Film.builder()
                .name("Inception")
                .description("A mind-bending thriller.")
                .releaseDate(LocalDate.of(2010, 7, 16))
                .duration(148)
                .build();
    }

    public static Film createFullFilm() {
        return Film.builder()
                .name("Inception")
                .description("A mind-bending thriller.")
                .releaseDate(LocalDate.of(2010, 7, 16))
                .mpa(MpaRating.builder().id(1L).build())
                .rate(100)
                .duration(148)
                .build();
    }
}
