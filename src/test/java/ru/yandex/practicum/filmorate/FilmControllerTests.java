package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnAllFilms() throws Exception {
        mockMvc.perform(get("/films"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldAddFilmWhenValidData() throws Exception {
        String newFilmJson = "{\"name\":\"Inception\",\"description\":\"A mind-bending thriller.\",\"releaseDate\":\"2010-07-16\",\"duration\":148}";

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newFilmJson))
                .andExpect(status().isOk())
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
        String invalidFilmJson = "{\"name\":\"\",\"description\":\"A mind-bending thriller.\",\"releaseDate\":\"2010-07-16\",\"duration\":148}";

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidFilmJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotAddFilmWhenInvalidDescription() throws Exception {
        String invalidFilmJson = "{\"name\":\"Inception\",\"description\":\"" + "A".repeat(201) + "\",\"releaseDate\":\"2010-07-16\",\"duration\":148}";

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidFilmJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotAddFilmWhenInvalidDuration() throws Exception {
        String invalidFilmJson = "{\"name\":\"Inception\",\"description\":\"A mind-bending thriller.\",\"releaseDate\":\"2010-07-16\",\"duration\":\"-148\"}";

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidFilmJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotUpdateFilmWhenNoId() throws Exception {
        String updateFilmWithoutIdJson = "{\"name\":\"Updated Film\",\"description\":\"Updated description.\",\"releaseDate\":\"2020-01-01\",\"duration\":\"120\"}";

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateFilmWithoutIdJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotUpdateFilmWhenNonExistentId() throws Exception {
        String nonExistentFilmJson = "{\"id\":999,\"name\":\"Non-Existent Film\",\"description\":\"Non-existent description.\",\"releaseDate\":\"2020-01-01\",\"duration\":\"120\"}";

        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nonExistentFilmJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotUpdateFilmWhenEmptyBody() throws Exception {
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isInternalServerError());
    }
}
