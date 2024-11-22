package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class Film {
    public Long id;
    public String name;
    public String description;
    public Integer rate;
    public MpaRating mpa;
    public List<Genre> genres;
    public LocalDate releaseDate;
    public Integer duration;
}
