package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.utils.FilmBuilder;
import ru.yandex.practicum.filmorate.utils.UserBuilder;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTests {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final FilmService filmService;

    @Autowired
    public FilmControllerTests(MockMvc mockMvc, ObjectMapper objectMapper, UserService userService, FilmService filmService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.filmService = filmService;
    }

    @Test
    public void shouldReturnAllFilms() throws Exception {
        mockMvc.perform(get("/films"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldAddFilmWhenValidData() throws Exception {
        Film newFilm = Film.builder()
                .name("Inception")
                .description("A mind-bending thriller.")
                .releaseDate(LocalDate.of(2010, 7, 16))
                .duration(148)
                .build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newFilm)))  // Преобразование объекта Film в JSON
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.name", is("Inception")))
                .andExpect(jsonPath("$.description", is("A mind-bending thriller.")));
    }

    @Test
    public void shouldNotAddFilmWhenEmptyBody() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotAddFilmWhenInvalidName() throws Exception {
        Film invalidFilm = Film.builder()
                .name("")
                .description("A mind-bending thriller.")
                .releaseDate(LocalDate.of(2010, 7, 16))
                .duration(148)
                .build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidFilm)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotAddFilmWhenInvalidDescription() throws Exception {
        Film invalidFilm = Film.builder()
                .name("Inception")
                .description("A".repeat(201))  // Генерация длинного описания
                .releaseDate(LocalDate.of(2010, 7, 16))
                .duration(148)
                .build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidFilm)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotAddFilmWhenInvalidDuration() throws Exception {
        Film invalidFilm = Film.builder()
                .name("Inception")
                .description("A mind-bending thriller.")
                .releaseDate(LocalDate.of(2010, 7, 16))
                .duration(-148)  // Неверная продолжительность (отрицательное значение)
                .build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidFilm)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotUpdateFilmWhenNoId() throws Exception {
        Film filmWithoutId = Film.builder()
                .name("Updated Film")
                .description("Updated description.")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(120)
                .build();

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmWithoutId)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotUpdateFilmWhenNonExistentId() throws Exception {
        Film nonExistentFilm = Film.builder()
                .id(999L)
                .name("Non-Existent Film")
                .description("Non-existent description.")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(120)
                .build();

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nonExistentFilm)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldNotUpdateFilmWhenEmptyBody() throws Exception {
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldAddLike() throws Exception {
        Film newFilm = filmService.addFilm(FilmBuilder.createFilm());
        User newUser = userService.addUser(UserBuilder.createUser());

        mockMvc.perform(put(String.format("/films/%d/like/%d", newFilm.getId(), newUser.getId())))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotAddSecondLike() throws Exception {
        Film newFilm = filmService.addFilm(FilmBuilder.createFilm());
        User newUser = userService.addUser(UserBuilder.createUser());

        mockMvc.perform(put(String.format("/films/%d/like/%d", newFilm.getId(), newUser.getId())))
                .andExpect(status().isOk());
        mockMvc.perform(put(String.format("/films/%d/like/%d", newFilm.getId(), newUser.getId())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRemoveLike() throws Exception {
        Film newFilm = filmService.addFilm(FilmBuilder.createFilm());
        User newUser = userService.addUser(UserBuilder.createUser());

        mockMvc.perform(put(String.format("/films/%d/like/%d", newFilm.getId(), newUser.getId())))
                .andExpect(status().isOk());
        mockMvc.perform(delete(String.format("/films/%d/like/%d", newFilm.getId(), newUser.getId())))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetMostPopular() throws Exception {
        int maxCount = 12;

        for (int i = 0; i < maxCount; i++) {
            Film newFilm = filmService.addFilm(FilmBuilder.createFilm());

            for (int j = 0; j < i + 1; j++) {
                User newUser = userService.addUser(UserBuilder.createUser());

                filmService.addLike(newUser.getId(), newFilm.getId());
            }
        }

        mockMvc.perform(get("/films/popular"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(equalTo(10)))
                .andExpect(jsonPath("$[0].rate").value(equalTo(maxCount)));
        mockMvc.perform(get("/films/popular?count=12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(equalTo(maxCount)))
                .andExpect(jsonPath("$[0].rate").value(equalTo(maxCount)));
    }
}