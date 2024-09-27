package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FilmRepository {
    int id = 1;
    Map<Integer, Film> films = new HashMap<>();

    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    public Film add(Film film) {
        film.setId(nextId());
        return films.put(film.getId(), film);
    }

    public Film update(int id, Film film) {
        return films.put(id, film);
    }

    public boolean contains(Film film) {
        return films.containsKey(film.getId());
    }

    private int nextId() {
        return id++;
    }
}
