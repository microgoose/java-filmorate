package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class UserFriend {
    public Long id;
    public Long userId;
    public Long friendId;
}
