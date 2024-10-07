package ru.yandex.practicum.filmorate.utils;

import java.util.Map;

public class MapUtils {
    public static <T> long getNextId (Map<Long, T> map) {
        long currentMaxId = map.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
