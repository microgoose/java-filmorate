package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    public Integer id;
    public String email;
    public String login;
    public String name;
    public LocalDate birthday;
}
