package ru.yandex.practicum.filmorate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGetUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAddUser() throws Exception {
        String newUserJson = "{\"email\":\"test@mail.com\",\"login\":\"testLogin\",\"birthday\":\"2000-01-01\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isOk())
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
        String newUserJson = "{\"email\":\"test2@mail.com\",\"login\":\"testLogin2\",\"birthday\":\"2000-01-01\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("testLogin2"))); // Имя устанавливается как логин, если пустое
    }

    @Test
    void shouldNotAddUserWithIncorrectEmail() throws Exception {
        String newUserJson = "{\"email\":\"invalidemail\",\"login\":\"testLogin\",\"birthday\":\"2000-01-01\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotAddUserWithoutPassword() throws Exception {
        String newUserJson = "{\"email\":\"test@mail.com\",\"login\":\"\",\"birthday\":\"2000-01-01\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotAddUserWithBirthdayInFuture() throws Exception {
        String newUserJson = "{\"email\":\"test@mail.com\",\"login\":\"testLogin\",\"birthday\":\"3000-01-01\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateUser() throws Exception {
        String newUserJson = "{\"email\":\"test@mail.com\",\"login\":\"testLogin\",\"birthday\":\"2000-01-01\"}";
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson));

        String updatedUserJson = "{\"id\":1,\"email\":\"updated@mail.com\",\"login\":\"updatedLogin\",\"birthday\":\"1995-05-05\"}";

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("updated@mail.com")))
                .andExpect(jsonPath("$.login", is("updatedLogin")));
    }

    @Test
    void shouldNotUpdateWithEmptyBody() throws Exception {
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldNotUpdateUserWithoutId() throws Exception {
        String updateUserWithoutIdJson = "{\"email\":\"updated@mail.com\",\"login\":\"updatedLogin\",\"birthday\":\"1995-05-05\"}";

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateUserWithoutIdJson))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldNotUpdateNonExistUser() throws Exception {
        String nonExistentUserJson = "{\"id\":999,\"email\":\"nonexist@mail.com\",\"login\":\"nonExist\",\"birthday\":\"1990-01-01\"}";

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nonExistentUserJson))
                .andExpect(status().isInternalServerError());
    }
}
