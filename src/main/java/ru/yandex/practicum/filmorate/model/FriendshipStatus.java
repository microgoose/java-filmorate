package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FriendshipStatus {
    public Long id;
    public String name;
}
