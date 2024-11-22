package ru.yandex.practicum.filmorate.storage.db.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MpaRatingDbMapper implements RowMapper<MpaRating> {
    @Override
    public MpaRating mapRow(ResultSet rs, int rowNum) throws SQLException {
        return MpaRating.builder()
                .id(rs.getLong("mpa_rating_id"))
                .name(rs.getString("name"))
                .build();
    }
}
