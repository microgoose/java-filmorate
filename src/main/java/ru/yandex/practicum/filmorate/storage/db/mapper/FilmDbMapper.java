package ru.yandex.practicum.filmorate.storage.db.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FilmDbMapper implements RowMapper<Film> {
    private final GenreService genreService;
    private final MpaRatingStorage mpaRatingStorage;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        LocalDate releaseDate = rs.getDate("release_date") != null
                ? rs.getDate("release_date").toLocalDate()
                : null;

        long filmId = rs.getLong("film_id");
        List<Genre> genres = genreService.findByFilm(filmId);
        Optional<MpaRating> optionalMpaRating = mpaRatingStorage.findById(rs.getLong("mpa_rating_id"));
        MpaRating mpaRating = optionalMpaRating.orElse(null);

        return Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .rate(rs.getInt("rate"))
                .mpa(mpaRating)
                .genres(genres)
                .releaseDate(releaseDate)
                .duration(rs.getInt("duration"))
                .build();
    }
}
