package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FilmGenre {
    public Film film;
    public Genre genre;

    public Long getGenreId() {
        if (genre == null) return null;
        return genre.getId();
    }

    public Long getFilmId() {
        if (film == null) return null;
        return film.getId();
    }
}
