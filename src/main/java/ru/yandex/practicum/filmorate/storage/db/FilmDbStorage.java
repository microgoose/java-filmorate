package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.db.mapper.FilmDbMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final NamedParameterJdbcTemplate jdbc;
    private final FilmDbMapper mapper;

    @Override
    public List<Film> getAll() {
        String filmsTableQuery = "SELECT f.* FROM film f order by f.film_id";

        return jdbc.query(filmsTableQuery, mapper);
    }

    @Override
    public Optional<Film> get(long id) {
        String query = "SELECT f.* FROM film f WHERE film_id = :id";
        SqlParameterSource params = new MapSqlParameterSource().addValue("id", id);

        try {
            return Optional.ofNullable(jdbc.queryForObject(query, params, mapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Film add(Film film) {
        String query =
                "INSERT INTO film (name, description, rate, mpa_rating_id, release_date, duration) " +
                "VALUES (:name, :description, :rate, :mpaRatingId, :releaseDate, :duration)";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("rate", film.getRate())
                .addValue("mpaRatingId", film.getMpa() == null ? null : film.getMpa().getId())
                .addValue("releaseDate", film.getReleaseDate())
                .addValue("duration", film.getDuration());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(query, params, keyHolder, new String[]{"film_id"});
        Number nextId = keyHolder.getKey();
        Long id = nextId != null ? nextId.longValue() : null;

        if (id == null) {
            throw new RuntimeException("Не удалось добавить фильм!");
        }

        film.setId(id);
        return film;
    }

    @Override
    public Film update(Film film) {
        String query =
                "UPDATE film " +
                "SET name = :name, description = :description, rate = :rate, " +
                    "mpa_rating_id = :mpaId, release_date = :releaseDate, duration = :duration " +
                "WHERE film_id = :filmId";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("filmId", film.getId())
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("rate", film.getRate())
                .addValue("mpaId", film.getMpa() == null ? null : film.getMpa().getId())
                .addValue("releaseDate", film.getReleaseDate())
                .addValue("duration", film.getDuration());

        int rowsUpdated = jdbc.update(query, params);

        if (rowsUpdated == 0) {
            throw new RuntimeException("Не удалось обновить данные");
        }

        return film;
    }

    @Override
    public List<Film> getMostPopular(int count) {
        String query = "SELECT f.* FROM film f ORDER BY rate DESC LIMIT :count";
        SqlParameterSource params = new MapSqlParameterSource().addValue("count", count);

        return jdbc.query(query, params, mapper);
    }

    @Override
    public boolean contains(long id) {
        String query = "SELECT 1 FROM film WHERE film_id =:id LIMIT 1";
        SqlParameterSource params = new MapSqlParameterSource().addValue("id", id);

        try {
            jdbc.queryForObject(query, params, Boolean.class);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
