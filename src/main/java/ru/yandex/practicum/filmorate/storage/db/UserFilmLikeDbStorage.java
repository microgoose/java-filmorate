package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.UserFilmLikeStorage;

@Repository
@RequiredArgsConstructor
public class UserFilmLikeDbStorage implements UserFilmLikeStorage {
    private final JdbcTemplate jdbc;

    @Override
    public void addLike(long userId, long filmId) {
        String query = "INSERT INTO user_film_like (user_id, film_id) VALUES (?, ?)";
        jdbc.update(query, userId, filmId);
    }

    @Override
    public void removeLike(long userId, long filmId) {
        String query = "DELETE FROM user_film_like WHERE user_id = ? and film_id = ?";
        jdbc.update(query, userId, filmId);
    }

    @Override
    public boolean contains(long userId, long filmId) {
        String query = "SELECT 1 FROM user_film_like WHERE user_id = ? and film_id = ? LIMIT 1";

        try {
            jdbc.queryForObject(query, Boolean.class, userId, filmId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
