package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;
import ru.yandex.practicum.filmorate.storage.db.mapper.MpaRatingDbMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MpaRatingDbStorage implements MpaRatingStorage {
    private final JdbcTemplate jdbc;
    private final MpaRatingDbMapper mapper;

    @Override
    public List<MpaRating> findAll() {
        String sql = "SELECT * FROM mpa_rating order by MPA_RATING_ID";
        return jdbc.query(sql, mapper);
    }

    @Override
    public Optional<MpaRating> findById(long mpaRatingId) {
        String sql = "SELECT * FROM mpa_rating WHERE mpa_rating_id = ?";

        try {
            return Optional.ofNullable(jdbc.queryForObject(sql, mapper, mpaRatingId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean contains(long mpaRatingId) {
        String query = "SELECT 1 FROM mpa_rating WHERE mpa_rating_id = ? LIMIT 1";

        try {
            jdbc.queryForObject(query, Boolean.class, mpaRatingId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
