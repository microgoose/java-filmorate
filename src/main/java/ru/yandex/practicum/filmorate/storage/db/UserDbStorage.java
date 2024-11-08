package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.db.mapper.UserDbMapper;
import ru.yandex.practicum.filmorate.util.SQLOperationUtil;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbc;
    private final UserDbMapper mapper;

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbc.query(sql, mapper);
    }

    @Override
    public Optional<User> findById(long userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        return jdbc.query(sql, mapper, userId).stream().findFirst();
    }

    @Override
    public List<User> getFriends(long userId) {
        String sql =
                "SELECT fu.* FROM user_friend uf " +
                "INNER JOIN users fu ON uf.friend_id = fu.user_id " +
                "WHERE uf.user_id = ?";

        return jdbc.query(sql, mapper, userId);
    }

    @Override
    public List<User> getCommonFriends(long firstUserId, long secondUserId) {
        String sql =
            "SELECT u2.* FROM user_friend uf1 " +
            "INNER JOIN user_friend uf2 ON uf1.friend_id = uf2.friend_id " +
            "INNER JOIN users u2 ON uf2.friend_id = u2.user_id " +
            "WHERE uf1.user_id = ? AND uf2.user_id = ?";

        return jdbc.query(sql, mapper, firstUserId, secondUserId);
    }

    @Override
    public User add(User user) {
        String query = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
        Long id = SQLOperationUtil.insert(jdbc, query,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());

        if (id == null) {
            throw new RuntimeException("Не удалось добавить пользователя!");
        }

        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        String query = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
        int rowsUpdated = jdbc.update(query,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        if (rowsUpdated == 0) {
            throw new RuntimeException("Не удалось обновить данные");
        }

        return user;
    }

    @Override
    public boolean contains(long userId) {
        String query = "SELECT 1 FROM users WHERE user_id = ? LIMIT 1";

        try {
            jdbc.queryForObject(query, Boolean.class, userId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
