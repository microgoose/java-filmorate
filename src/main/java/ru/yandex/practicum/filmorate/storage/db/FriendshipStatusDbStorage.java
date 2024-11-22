package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.storage.FriendshipStatusStorage;
import ru.yandex.practicum.filmorate.storage.db.mapper.FriendshipStatusDbMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FriendshipStatusDbStorage implements FriendshipStatusStorage {
    private final JdbcTemplate jdbc;
    private final FriendshipStatusDbMapper mapper;

    @Override
    public List<FriendshipStatus> findAll() {
        String sql = "SELECT friendship_status_id, name FROM friendship_status";
        return jdbc.query(sql, mapper);
    }

    @Override
    public Optional<FriendshipStatus> findById(long id) {
        String sql = "SELECT friendship_status_id, name FROM friendship_status WHERE friendship_status_id = ?";
        return jdbc.query(sql, mapper, id).stream().findFirst();
    }
}
