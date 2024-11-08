package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.db.mapper.GenreDbMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbc;
    private final GenreDbMapper mapper;

    @Override
    public List<Genre> findAll() {
        String sql = "SELECT * FROM genre order by genre_id";
        return jdbc.query(sql, mapper);
    }

    @Override
    public Optional<Genre> findById(long genreId) {
        String sql = "SELECT * FROM genre WHERE genre_id = ?";
        return jdbc.query(sql, mapper, genreId).stream().findFirst();
    }

    @Override
    public List<Genre> findById(List<Long> genresId) {
        String genresIdParam = genresId.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
        String sql = "SELECT * FROM genre WHERE genre_id in (" + genresIdParam + ")";
        return jdbc.query(sql, mapper);
    }

    @Override
    public List<Genre> findByFilm(long filmId) {
        String query =
            "select distinct g.* from film_genre fg " +
            "inner join genre g on g.genre_id = fg.genre_id " +
            "where film_id = ? ";

        return jdbc.query(query, mapper, filmId);
    }
}
