package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserFriend {
    public User user;
    public User friend;
    public FriendshipStatus friendshipStatus;
}
