package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FilmGenreService {
    private final FilmGenreStorage filmGenresStorage;
    private final GenreService genreService;

    @Autowired
    public FilmGenreService(FilmGenreStorage filmGenreStorage, GenreService genreService) {
        this.filmGenresStorage = filmGenreStorage;
        this.genreService = genreService;
    }

    public List<FilmGenre> add(Film film) {
        validateGenres(film.getGenres());

        List<FilmGenre> filmGenres = film.getGenres().stream()
                .map((Genre genre) -> FilmGenre.builder().film(film).genre(genre).build())
                .toList();

        filmGenresStorage.add(filmGenres);
        return filmGenres;
    }

    public List<FilmGenre> update(Film film) {
        validateGenres(film.getGenres());

        List<Genre> existFilmGenres = genreService.findByFilm(film.getId());
        List<FilmGenre> filmGenres = film.getGenres().stream()
                .filter((filmGenre) -> existFilmGenres.stream()
                        .noneMatch(existFilmGenre -> existFilmGenre.equals(filmGenre)))
                .map((filmGenre) -> FilmGenre.builder().film(film).genre(filmGenre).build())
                .toList();

        filmGenresStorage.add(filmGenres);
        return filmGenres;
    }

    public void validateGenres(List<Genre> filmGenres) {
        List<Long> filmGenresId = filmGenres.stream().map(Genre::getId).collect(Collectors.toList());
        List<Genre> existGenres = genreService.findById(filmGenresId);

        if (filmGenresId.size() != existGenres.size()) {
            List<Long> nonExistentGenres = filmGenresId.stream()
                    .filter(genreId -> existGenres.stream()
                            .noneMatch(existGenre -> Objects.equals(existGenre.getId(), genreId)))
                    .toList();

            throw new ValidationException("Не существует жанров: " + nonExistentGenres);
        }
    }
}
