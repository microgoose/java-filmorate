package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {
    int id = 1;
    Map<Integer, User> users = new HashMap<>();

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public User add(User user) {
        user.setId(nextId());
        return users.put(user.getId(), user);
    }

    public User update(int id, User user) {
        return users.put(id, user);
    }

    public boolean contains(User user) {
        return users.containsKey(user.getId());
    }

    private int nextId() {
        return id++;
    }
}
