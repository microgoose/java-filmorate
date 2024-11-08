package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FilmGenreDbStorage implements FilmGenreStorage {
    private final JdbcTemplate jdbc;

    @Override
    public List<FilmGenre> add(List<FilmGenre> filmGenres) {
        String sql = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
        jdbc.batchUpdate(sql, filmGenres, filmGenres.size(), (ps, filmGenre) -> {
            ps.setLong(1, filmGenre.getFilm().getId());
            ps.setLong(2, filmGenre.getGenre().getId());
        });
        return filmGenres;
    }
}
