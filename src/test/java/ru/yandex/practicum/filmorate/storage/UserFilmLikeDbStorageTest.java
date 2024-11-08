package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.FilmBuilder;
import ru.yandex.practicum.filmorate.utils.UserBuilder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserFilmLikeDbStorageTest {

    private final UserFilmLikeStorage userFilmLikeStorage;
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    @Test
    public void testAddLike() {
        // Создаем тестовые объекты
        User user = userStorage.add(UserBuilder.createUser());
        Film film = filmStorage.add(FilmBuilder.createFullFilm());

        // Добавляем лайк
        userFilmLikeStorage.addLike(user.getId(), film.getId());

        // Проверяем, что лайк был добавлен
        boolean likeExists = userFilmLikeStorage.contains(user.getId(), film.getId());
        assertThat(likeExists).isTrue();
    }

    @Test
    public void testRemoveLike() {
        // Создаем тестовые объекты
        User user = userStorage.add(UserBuilder.createUser());
        Film film = filmStorage.add(FilmBuilder.createFullFilm());

        // Добавляем лайк и затем удаляем его
        userFilmLikeStorage.addLike(user.getId(), film.getId());
        userFilmLikeStorage.removeLike(user.getId(), film.getId());

        // Проверяем, что лайк был удален
        boolean likeExists = userFilmLikeStorage.contains(user.getId(), film.getId());
        assertThat(likeExists).isFalse();
    }

    @Test
    public void testContainsLike() {
        // Создаем тестовые объекты
        User user = userStorage.add(UserBuilder.createUser());
        Film film = filmStorage.add(FilmBuilder.createFullFilm());

        // Проверяем, что лайк не существует
        boolean likeExistsBefore = userFilmLikeStorage.contains(user.getId(), film.getId());
        assertThat(likeExistsBefore).isFalse();

        // Добавляем лайк
        userFilmLikeStorage.addLike(user.getId(), film.getId());

        // Проверяем, что лайк существует
        boolean likeExistsAfter = userFilmLikeStorage.contains(user.getId(), film.getId());
        assertThat(likeExistsAfter).isTrue();
    }
}
