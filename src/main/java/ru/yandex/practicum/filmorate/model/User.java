package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {
    public Long id;
    public String email;
    public String login;
    public String name;
    public LocalDate birthday;
}
