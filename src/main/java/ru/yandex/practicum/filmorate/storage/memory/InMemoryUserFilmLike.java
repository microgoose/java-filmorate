package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFilmLike;
import ru.yandex.practicum.filmorate.storage.UserFilmLikeStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class InMemoryUserFilmLike implements UserFilmLikeStorage {
    List<UserFilmLike> userFilmLikes = new ArrayList<>();

    @Override
    public List<UserFilmLike> getAll() {
        return userFilmLikes;
    }

    @Override
    public List<UserFilmLike> getLikes(User user) {
        return userFilmLikes.stream()
                .filter(uf -> Objects.equals(uf.getUserId(), user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserFilmLike> getLikes(Film film) {
        return userFilmLikes.stream()
                .filter(uf -> Objects.equals(uf.getFilmId(), film.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public UserFilmLike addLike(User user, Film film) {
        UserFilmLike userFilmLike = new UserFilmLike();
        userFilmLike.setId((long) userFilmLikes.size());
        userFilmLike.setUserId(user.getId());
        userFilmLike.setFilmId(film.getId());

        userFilmLikes.add(userFilmLike);
        return userFilmLike;
    }

    @Override
    public void removeLike(User user, Film film) {
        UserFilmLike userFilmLike = userFilmLikes.stream()
                .filter(uf ->
                    Objects.equals(uf.getUserId(), user.getId()) &&
                    Objects.equals(uf.getFilmId(), film.getId())
                ).findFirst().orElse(null);

        if (userFilmLike == null)
            return;

        userFilmLikes.remove(userFilmLike);
    }
}
