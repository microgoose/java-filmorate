package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTests {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserService userService;

    @Autowired
    public UserControllerTests(MockMvc mockMvc, ObjectMapper objectMapper, UserService userService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    @Test
    void shouldGetUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAddUser() throws Exception {
        User newUser = User.builder()
                .email("test@mail.com")
                .login("testLogin")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.email", is("test@mail.com")))
                .andExpect(jsonPath("$.login", is("testLogin")))
                .andExpect(jsonPath("$.birthday", is("2000-01-01")));
    }

    @Test
    void shouldNotAddWithEmptyBody() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldAddUserAndSetName() throws Exception {
        User newUser = User.builder()
                .email("test2@mail.com")
                .login("testLogin2")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("testLogin2"))); // Имя устанавливается как логин, если пустое
    }

    @Test
    void shouldNotAddUserWithIncorrectEmail() throws Exception {
        User newUser = User.builder()
                .email("invalidemail")
                .login("testLogin")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotAddUserWithoutPassword() throws Exception {
        User newUser = User.builder()
                .email("test@mail.com")
                .login("")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotAddUserWithBirthdayInFuture() throws Exception {
        User newUser = User.builder()
                .email("test@mail.com")
                .login("testLogin")
                .birthday(LocalDate.of(3000, 1, 1))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateUser() throws Exception {
        User newUser = User.builder()
                .email("test@mail.com")
                .login("testLogin")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)));

        User updatedUser = User.builder()
                .id(1L)
                .email("updated@mail.com")
                .login("updatedLogin")
                .birthday(LocalDate.of(1995, 5, 5))
                .build();

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("updated@mail.com")))
                .andExpect(jsonPath("$.login", is("updatedLogin")));
    }

    @Test
    void shouldNotUpdateWithEmptyBody() throws Exception {
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotUpdateUserWithoutId() throws Exception {
        User updatedUserWithoutId = User.builder()
                .email("updated@mail.com")
                .login("updatedLogin")
                .birthday(LocalDate.of(1995, 5, 5))
                .build();

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUserWithoutId)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotUpdateNonExistUser() throws Exception {
        User nonExistentUser = User.builder()
                .id(999L)
                .email("nonexist@mail.com")
                .login("nonExist")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nonExistentUser)))
                .andExpect(status().isNotFound());
    }


    @Test
    public void shouldAddFriend() throws Exception {
        User user = userService.addUser(createUser());
        User friend = userService.addUser(createUser());

        mockMvc.perform(put(String.format("/users/%d/friends/%d", user.getId(), friend.getId())))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotAddExistFriend() throws Exception {
        User user = userService.addUser(createUser());
        User friend = userService.addUser(createUser());

        mockMvc.perform(put(String.format("/users/%d/friends/%d", user.getId(), friend.getId())))
                .andExpect(status().isOk());
        mockMvc.perform(put(String.format("/users/%d/friends/%d", user.getId(), friend.getId())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotAddNonExistFriendUser() throws Exception {
        User user = userService.addUser(createUser());

        mockMvc.perform(put(String.format("/users/%d/friends/%d", user.getId(), -1)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldRemoveFriend() throws Exception {
        User user = userService.addUser(createUser());
        User friend = userService.addUser(createUser());

        mockMvc.perform(put(String.format("/users/%d/friends/%d", user.getId(), friend.getId())))
                .andExpect(status().isOk());
        mockMvc.perform(delete(String.format("/users/%d/friends/%d", user.getId(), friend.getId())))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotRemoveNonExistFriend() throws Exception {
        User user = userService.addUser(createUser());

        mockMvc.perform(delete(String.format("/users/%d/friends/%d", user.getId(), -1)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetUserFriends() throws Exception {
        User user = userService.addUser(createUser());
        User friend = userService.addUser(createUser());
        User friend1 = userService.addUser(createUser());

        userService.addFriend(user.getId(), friend.getId());
        userService.addFriend(user.getId(), friend1.getId());

        mockMvc.perform(get(String.format("/users/%d/friends", user.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(equalTo(2)))
                .andExpect(jsonPath("$[0].id").value(equalTo(friend.getId().intValue())));
    }

    @Test
    public void shouldReturnCommonFriends() throws Exception {
        User user = userService.addUser(createUser());
        User user2 = userService.addUser(createUser());
        User friendWithReducedSocialResponsibility = userService.addUser(createUser());

        userService.addFriend(user.getId(), friendWithReducedSocialResponsibility.getId());
        userService.addFriend(user2.getId(), friendWithReducedSocialResponsibility.getId());

        mockMvc.perform(get(String.format("/users/%d/friends/common/%d", user.getId(), user2.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(equalTo(1)))
                .andExpect(jsonPath("$[0].id")
                        .value(equalTo(friendWithReducedSocialResponsibility.getId().intValue())));
    }

    private User createUser() {
        return User.builder()
                .email("mail@mail.com")
                .login("Name")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
    }
}
