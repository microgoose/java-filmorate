package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.db.mapper.UserDbMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final NamedParameterJdbcTemplate jdbc;
    private final UserDbMapper mapper;

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbc.query(sql, mapper);
    }

    @Override
    public Optional<User> findById(long userId) {
        String sql = "SELECT * FROM users WHERE user_id = :userId";
        MapSqlParameterSource params = new MapSqlParameterSource("userId", userId);

        return jdbc.query(sql, params, mapper).stream().findFirst();
    }

    @Override
    public List<User> getFriends(long userId) {
        String sql =
                "SELECT fu.* FROM user_friend uf " +
                "INNER JOIN users fu ON uf.friend_id = fu.user_id " +
                "WHERE uf.user_id = :userId";

        MapSqlParameterSource params = new MapSqlParameterSource("userId", userId);

        return jdbc.query(sql, params, mapper);
    }

    @Override
    public List<User> getCommonFriends(long firstUserId, long secondUserId) {
        String sql =
                "SELECT u2.* FROM user_friend uf1 " +
                "INNER JOIN user_friend uf2 ON uf1.friend_id = uf2.friend_id " +
                "INNER JOIN users u2 ON uf2.friend_id = u2.user_id " +
                "WHERE uf1.user_id = :firstUserId AND uf2.user_id = :secondUserId";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("firstUserId", firstUserId)
                .addValue("secondUserId", secondUserId);

        return jdbc.query(sql, params, mapper);
    }

    @Override
    public User add(User user) {
        String query =
                "INSERT INTO users (email, login, name, birthday) " +
                "VALUES (:email, :login, :name, :birthday)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", user.getBirthday());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(query, params, keyHolder, new String[]{"user_id"});
        Number nextId = keyHolder.getKey();
        Long id = nextId != null ? nextId.longValue() : null;

        if (id == null) {
            throw new RuntimeException("Не удалось добавить пользователя!");
        }

        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        String query =
                "UPDATE users SET email = :email, login = :login, name = :name, birthday = :birthday " +
                "WHERE user_id = :userId";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", user.getBirthday())
                .addValue("userId", user.getId());

        int rowsUpdated = jdbc.update(query, params);

        if (rowsUpdated == 0) {
            throw new RuntimeException("Не удалось обновить данные");
        }

        return user;
    }

    @Override
    public boolean contains(long userId) {
        String query = "SELECT 1 FROM users WHERE user_id = :userId LIMIT 1";
        MapSqlParameterSource params = new MapSqlParameterSource("userId", userId);

        try {
            jdbc.queryForObject(query, params, Boolean.class);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
