package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    public Long id;
    public String name;
    public String description;
    public Integer rate;
    public LocalDate releaseDate;
    public @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT) Duration duration;
}
