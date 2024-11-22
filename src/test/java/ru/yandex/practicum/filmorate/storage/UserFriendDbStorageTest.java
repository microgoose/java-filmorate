package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.UserBuilder;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserFriendDbStorageTest {
    private final UserFriendStorage userFriendStorage;
    private final UserStorage userStorage;

    @Test
    public void testAddFriend() {
        // Создаем двух пользователей
        User user1 = userStorage.add(UserBuilder.createUser(
                "testAddFriend1@example.com", "testAddFriend1", "User One",
                LocalDate.of(1990, 1, 1)));
        User user2 = userStorage.add(UserBuilder.createUser(
                "testAddFriend2@example.com", "testAddFriend2", "User Two",
                LocalDate.of(1992, 2, 2)));

        // Добавляем user2 в друзья к user1
        userFriendStorage.add(user1.getId(), user2.getId(), 1L); // статус 1 — "друзья"

        // Проверяем, что user2 теперь находится в списке друзей user1
        List<User> friends = userStorage.getFriends(user1.getId());
        assertThat(friends).hasSize(1);
        assertThat(friends.get(0).getId()).isEqualTo(user2.getId());
        assertThat(friends.get(0).getName()).isEqualTo("User Two");
    }

    @Test
    public void testRemoveFriend() {
        // Создаем двух пользователей
        User user1 = userStorage.add(UserBuilder.createUser("user3@example.com", "login3", "User Three", LocalDate.of(1993, 3, 3)));
        User user2 = userStorage.add(UserBuilder.createUser("user4@example.com", "login4", "User Four", LocalDate.of(1994, 4, 4)));

        // Добавляем user2 в друзья к user1
        userFriendStorage.add(user1.getId(), user2.getId(), 1L);

        // Проверяем, что user2 находится в списке друзей user1
        List<User> friendsBeforeRemove = userStorage.getFriends(user1.getId());
        assertThat(friendsBeforeRemove).hasSize(1);

        // Удаляем user2 из друзей user1
        userFriendStorage.remove(user1.getId(), user2.getId());

        // Проверяем, что user2 больше не находится в списке друзей user1
        List<User> friendsAfterRemove = userStorage.getFriends(user1.getId());
        assertThat(friendsAfterRemove).isEmpty();
    }
}
