package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.utils.FilmBuilder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {
    private final GenreStorage genreStorage;
    private final FilmDbStorage filmDbStorage;
    private final FilmGenreStorage filmGenreStorage;

    @Test
    public void testFindAllGenres() {
        // Получаем все жанры
        List<Genre> genres = genreStorage.findAll();

        // Проверяем, что список жанров не пустой
        assertThat(genres).isNotNull().hasSizeGreaterThanOrEqualTo(1);

        // Проверяем наличие первого жанра
        assertThat(genres)
                .extracting(Genre::getId)
                .contains(1L);
    }

    @Test
    public void testFindGenreById() {
        // Ищем жанр по ID
        Optional<Genre> genreOptional = genreStorage.findById(1L);

        // Проверяем, что жанр найден и имеет правильные значения
        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre)
                                .hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrProperty("name"));
    }

    @Test
    public void testFindGenresByIds() {
        // Ищем несколько жанров по их идентификаторам
        List<Long> genresIds = List.of(1L, 2L);
        List<Genre> genres = genreStorage.findById(genresIds);

        // Проверяем, что найдены оба жанра
        assertThat(genres).isNotNull().hasSize(2);
        assertThat(genres)
                .extracting(Genre::getId)
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    public void testFindGenresByFilmId() {
        if (!filmDbStorage.contains(1L)) {
            filmDbStorage.add(FilmBuilder.createFullFilm());
            filmGenreStorage.add(List.of(FilmGenre.builder()
                    .film(Film.builder().id(1L).build())
                    .genre(Genre.builder().id(1L).build())
                    .build()));
        }

        List<Genre> genres = genreStorage.findByFilm(1L);
        assertThat(genres).isNotNull().hasSizeGreaterThanOrEqualTo(1);
    }
}
