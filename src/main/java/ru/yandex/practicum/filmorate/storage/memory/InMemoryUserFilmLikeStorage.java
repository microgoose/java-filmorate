package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.UserFilmLike;
import ru.yandex.practicum.filmorate.storage.UserFilmLikeStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class InMemoryUserFilmLikeStorage implements UserFilmLikeStorage {
    List<UserFilmLike> userFilmLikes = new ArrayList<>();

    @Override
    public List<UserFilmLike> getAll() {
        return userFilmLikes;
    }

    @Override
    public List<UserFilmLike> getLikesByUserId(long userId) {
        return userFilmLikes.stream()
                .filter(ufl -> Objects.equals(ufl.getUserId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserFilmLike> getLikesByFilmId(long filmId) {
        return userFilmLikes.stream()
                .filter(ufl -> Objects.equals(ufl.getFilmId(), filmId))
                .collect(Collectors.toList());
    }

    @Override
    public UserFilmLike addLike(long userId, long filmId) {
        UserFilmLike userFilmLike = UserFilmLike.builder()
                .id((long) userFilmLikes.size())
                .filmId(filmId)
                .userId(userId).build();

        userFilmLikes.add(userFilmLike);
        return userFilmLike;
    }

    @Override
    public void removeLike(long userId, long filmId) {
        UserFilmLike userFilmLike = userFilmLikes.stream()
                .filter(ufl ->
                    Objects.equals(ufl.getUserId(), userId) &&
                    Objects.equals(ufl.getFilmId(), filmId)
                ).findFirst().orElse(null);

        if (userFilmLike == null)
            return;

        userFilmLikes.remove(userFilmLike);
    }

    @Override
    public boolean containsLike(long userId, long filmId) {
        return userFilmLikes.stream().anyMatch(ufl ->
                Objects.equals(ufl.getUserId(), userId) && Objects.equals(ufl.getFilmId(), filmId));
    }
}
