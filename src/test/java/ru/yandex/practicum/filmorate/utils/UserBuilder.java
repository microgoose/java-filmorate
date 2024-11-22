package ru.yandex.practicum.filmorate.utils;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Random;

public class UserBuilder {
    public static User createUser() {
        return User.builder()
                .email(new Random().ints().toString() + "mail@mail.com")
                .login(new Random().ints().toString() + "Name")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
    }

    public static User createUser(String email, String login, String name, LocalDate birthday) {
        return User.builder()
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();
    }
}
