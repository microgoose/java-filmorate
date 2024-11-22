package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.utils.FilmBuilder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmGenreStorageTest {
    private final FilmStorage filmStorage;
    private final FilmGenreStorage filmGenreStorage;

    @Test
    public void testAddFilmGenres() {
        filmStorage.add(FilmBuilder.createFullFilm());

        FilmGenre genre1 = FilmGenre.builder()
                .film(Film.builder().id(1L).build())
                .genre(Genre.builder().id(1L).build())
                .build();
        FilmGenre genre2 = FilmGenre.builder()
                .film(Film.builder().id(1L).build())
                .genre(Genre.builder().id(2L).build())
                .build();

        List<FilmGenre> genresToAdd = List.of(genre1, genre2);
        List<FilmGenre> addedGenres = filmGenreStorage.add(genresToAdd);

        assertThat(addedGenres).isNotNull().hasSizeGreaterThanOrEqualTo(2);
        assertThat(addedGenres)
                .extracting(FilmGenre::getGenreId)
                .containsExactlyInAnyOrder(1L, 2L);
        assertThat(addedGenres)
                .extracting(FilmGenre::getFilmId)
                .containsOnly(1L);
    }
}