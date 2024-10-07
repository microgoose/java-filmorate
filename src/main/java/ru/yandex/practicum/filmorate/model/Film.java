package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    public Integer id;
    public String name;
    public String description;
    public LocalDate releaseDate;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    public Duration duration;
}
