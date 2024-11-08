package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.db.mapper.FilmDbMapper;
import ru.yandex.practicum.filmorate.util.SQLOperationUtil;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbc;
    private final FilmDbMapper mapper;

    @Override
    public List<Film> getAll() {
        String filmsTableQuery = "SELECT f.* FROM film f order by f.film_id";

        return jdbc.query(filmsTableQuery, mapper);
    }

    @Override
    public Optional<Film> get(long id) {
        String query = "SELECT f.* FROM film f WHERE film_id = ?";

        try {
            return Optional.ofNullable(jdbc.queryForObject(query, mapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Film add(Film film) {
        String query = "INSERT INTO film (name, description, rate, mpa_rating_id, release_date, duration) VALUES (?, ?, ?, ?, ?, ?)";
        Long id = SQLOperationUtil.insert(jdbc, query,
                film.getName(),
                film.getDescription(),
                film.getRate(),
                film.getMpa() == null ? null : film.getMpa().getId(),
                film.getReleaseDate(),
                film.getDuration());

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
                "SET name = ?, description = ?, rate = ?, mpa_rating_id = ?, release_date = ?, duration = ? " +
                "WHERE film_id = ?";

        int rowsUpdated = jdbc.update(query,
                film.getName(),
                film.getDescription(),
                film.getRate(),
                film.getMpa() == null ? null : film.getMpa().getId(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId());

        if (rowsUpdated == 0) {
            throw new RuntimeException("Не удалось обновить данные");
        }

        return film;
    }

    @Override
    public List<Film> getMostPopular(int count) {
        String query = "SELECT f.* FROM film f ORDER BY rate DESC LIMIT ?";

        return jdbc.query(query, mapper, count);
    }

    @Override
    public boolean contains(long id) {
        String query = "SELECT 1 FROM film WHERE film_id = ? LIMIT 1";

        try {
            jdbc.queryForObject(query, Boolean.class, id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
