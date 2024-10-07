package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
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
    public List<UserFriend> getFriends(User user) {
        return userFriends.stream()
                .filter(uf -> Objects.equals(uf.getUserId(), user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public UserFriend addFriend(User user, User friend) {
        UserFriend userFriend = new UserFriend();
        userFriend.setId((long) userFriends.size());
        userFriend.setUserId(user.getId());
        userFriend.setFriendId(friend.getId());

        userFriends.add(userFriend);
        return userFriend;
    }

    @Override
    public void removeFriend(User user, User friend) {
        UserFriend userFriend = userFriends.stream()
                .filter(uf ->
                    Objects.equals(uf.getUserId(), user.getId()) &&
                    Objects.equals(uf.getFriendId(), friend.getId())
                ).findFirst().orElse(null);

        if (userFriend == null)
            return;

        userFriends.remove(userFriend);
    }
}
