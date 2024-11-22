package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.utils.FilmBuilder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final FilmDbStorage storage;

    @Test
    public void testGetAll() {
        Film film = FilmBuilder.createFullFilm();
        storage.add(film);
        List<Film> films = storage.getAll();
        assertThat(films).isNotEmpty();
        assertThat(films).extracting(Film::getName).contains(film.getName());
    }

    @Test
    public void testGetFilm() {
        Film film = storage.add(FilmBuilder.createFullFilm());
        Optional<Film> filmOptional = storage.get(film.getId());

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(f ->
                        assertThat(f).hasFieldOrPropertyWithValue("id", film.getId())
                );
    }

    @Test
    public void testAddFilm() {
        Film newFilm = FilmBuilder.createFullFilm();
        Film addedFilm = storage.add(newFilm);

        assertThat(addedFilm).isNotNull();
        assertThat(addedFilm.getId()).isGreaterThan(0);
        assertThat(addedFilm.getName()).isEqualTo(newFilm.getName());
    }

    @Test
    public void testUpdateFilm() {
        Film film = storage.add(FilmBuilder.createFullFilm());
        film.setName("Updated Name");
        Film updatedFilm = storage.update(film);

        assertThat(updatedFilm).isNotNull();
        assertThat(updatedFilm.getId()).isEqualTo(film.getId());
        assertThat(updatedFilm.getName()).isEqualTo("Updated Name");
    }

    @Test
    public void testContains() {
        Film film = storage.add(FilmBuilder.createFullFilm());
        boolean exists = storage.contains(film.getId());

        assertThat(exists).isTrue();

        boolean nonExistent = storage.contains(999L);
        assertThat(nonExistent).isFalse();
    }
}
