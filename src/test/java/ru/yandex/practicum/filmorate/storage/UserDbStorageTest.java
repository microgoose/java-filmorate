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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private final UserStorage userStorage;
    private final UserFriendStorage userFriendStorage;

    @Test
    public void testAddUser() {
        User user = UserBuilder.createUser("test@example.com", "testLogin", "Test User", LocalDate.of(1990, 1, 1));
        User addedUser = userStorage.add(user);

        assertThat(addedUser).isNotNull();
        assertThat(addedUser.getId()).isNotNull();
        assertThat(addedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(addedUser.getLogin()).isEqualTo("testLogin");
        assertThat(addedUser.getName()).isEqualTo("Test User");
    }

    @Test
    public void testUpdateUser() {
        User user = UserBuilder.createUser("update@example.com", "updateLogin", "Update User", LocalDate.of(1995, 5, 5));
        User addedUser = userStorage.add(user);

        addedUser.setName("Updated Name");
        addedUser.setEmail("updated@example.com");

        User updatedUser = userStorage.update(addedUser);

        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
        assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");
    }

    @Test
    public void testFindAllUsers() {
        User user1 = userStorage.add(UserBuilder.createUser("user1@example.com", "login1", "User One", LocalDate.of(1990, 1, 1)));
        User user2 = userStorage.add(UserBuilder.createUser("user2@example.com", "login2", "User Two", LocalDate.of(1992, 2, 2)));

        List<User> users = userStorage.findAll();

        assertThat(users).hasSizeGreaterThanOrEqualTo(2);
        assertThat(users).extracting(User::getEmail).contains("user1@example.com", "user2@example.com");
    }

    @Test
    public void testFindById() {
        User user = userStorage.add(UserBuilder.createUser("find@example.com", "findLogin", "Find User", LocalDate.of(1988, 8, 8)));
        Optional<User> foundUser = userStorage.findById(user.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("find@example.com");
    }

    @Test
    public void testContainsUser() {
        User user = userStorage.add(UserBuilder.createUser("contains@example.com", "containsLogin", "Contains User", LocalDate.of(2000, 1, 1)));
        boolean exists = userStorage.contains(user.getId());

        assertThat(exists).isTrue();
        assertThat(userStorage.contains(99999L)).isFalse(); // Проверка на несуществующего пользователя
    }

    @Test
    public void testGetFriends() {
        User user1 = userStorage.add(UserBuilder.createUser("friend1@example.com", "friendLogin1", "Friend One", LocalDate.of(1990, 3, 3)));
        User user2 = userStorage.add(UserBuilder.createUser("friend2@example.com", "friendLogin2", "Friend Two", LocalDate.of(1991, 4, 4)));
        userFriendStorage.add(user1.getId(), user2.getId(), 1L);

        List<User> friends = userStorage.getFriends(user1.getId());

        assertThat(friends).hasSize(1);
        assertThat(friends.get(0).getId()).isEqualTo(user2.getId());
    }

    @Test
    public void testGetCommonFriends() {
        User user1 = userStorage.add(UserBuilder.createUser("common1@example.com", "commonLogin1", "Common User 1", LocalDate.of(1990, 5, 5)));
        User user2 = userStorage.add(UserBuilder.createUser("common2@example.com", "commonLogin2", "Common User 2", LocalDate.of(1991, 6, 6)));
        User commonFriend = userStorage.add(UserBuilder.createUser("friend@example.com", "friendLogin", "Common Friend", LocalDate.of(1985, 7, 7)));

        userFriendStorage.add(user1.getId(), commonFriend.getId(), 1L);
        userFriendStorage.add(user2.getId(), commonFriend.getId(), 1L);

        List<User> commonFriends = userStorage.getCommonFriends(user1.getId(), user2.getId());

        assertThat(commonFriends).hasSize(1);
        assertThat(commonFriends.get(0).getId()).isEqualTo(commonFriend.getId());
    }
}
