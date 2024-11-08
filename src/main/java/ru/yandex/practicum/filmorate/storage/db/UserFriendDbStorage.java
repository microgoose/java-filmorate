package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.UserFriendStorage;

@Repository
@RequiredArgsConstructor
public class UserFriendDbStorage implements UserFriendStorage {
    private final JdbcTemplate jdbc;

    @Override
    public void add(long userId, long friendId, long statusId) {
        String query = "INSERT INTO user_friend (user_id, friend_id, friendship_status_id) VALUES (?, ?, ?)";
        jdbc.update(query, userId, friendId, statusId);
    }

    @Override
    public void remove(long userId, long friendId) {
        String query = "DELETE FROM user_friend WHERE user_id = ? and friend_id = ?";
        jdbc.update(query, userId, friendId);
    }
}
