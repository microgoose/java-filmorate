package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository = new UserRepository();

    public List<User> getAll() {
        return userRepository.getAll();
    }

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isEmpty())
            user.setName(user.getLogin());

        validate(user);
        userRepository.add(user);
        return user;
    }

    public User updateUser(User user) {
        if (user.getId() == null)
            throw new IllegalArgumentException("Не передан id!");
        if (!userRepository.contains(user))
            throw new IllegalArgumentException("Не найден пользователь с таким id!");

        validate(user);
        userRepository.update(user.getId(), user);
        return user;
    }

    private void validate(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@"))
            throw new ValidationException("Неккоректный email!");
        if (user.getLogin() == null || user.getLogin().trim().isEmpty())
            throw new ValidationException("Логин не может быть пустым!");
        if (user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("Дата рождения не может быть больше текущей!");
    }
}
