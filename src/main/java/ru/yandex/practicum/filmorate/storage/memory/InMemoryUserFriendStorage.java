package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.UserFriend;
import ru.yandex.practicum.filmorate.storage.UserFriendStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class InMemoryUserFriendStorage implements UserFriendStorage {
    List<UserFriend> userFriends = new ArrayList<>();

    @Override
    public List<UserFriend> getFriends(Long userId) {
        return userFriends.stream()
                .filter(uf -> Objects.equals(uf.getUserId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public UserFriend addFriend(Long userId, Long friendId) {
        UserFriend userFriend = UserFriend.builder()
                .id((long) userFriends.size())
                .userId(userId)
                .friendId(friendId).build();

        userFriends.add(userFriend);
        return userFriend;
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        UserFriend userFriend = userFriends.stream()
                .filter(uf ->
                    Objects.equals(uf.getUserId(), userId) &&
                    Objects.equals(uf.getFriendId(), friendId)
                ).findFirst().orElse(null);

        if (userFriend == null) {
            return;
        }

        userFriends.remove(userFriend);
    }

    @Override
    public boolean containsFriend(Long user, Long friend) {
        return userFriends.stream().anyMatch(ufl ->
                Objects.equals(ufl.getUserId(), user) && Objects.equals(ufl.getFriendId(), friend));
    }
}
