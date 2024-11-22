package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaRatingDbStorageTest {
    private final MpaRatingStorage mpaRatingStorage;

    @Test
    public void testFindAllMpaRatings() {
        // Получаем все MPA рейтинги
        List<MpaRating> mpaRatings = mpaRatingStorage.findAll();

        // Проверяем, что список не пустой
        assertThat(mpaRatings).isNotNull().hasSizeGreaterThanOrEqualTo(1);

        // Проверяем, что существует рейтинг с ID 1
        assertThat(mpaRatings)
                .extracting(MpaRating::getId)
                .contains(1L);
    }

    @Test
    public void testFindMpaRatingById() {
        // Ищем MPA рейтинг по ID
        Optional<MpaRating> mpaRatingOptional = mpaRatingStorage.findById(1L);

        // Проверяем, что рейтинг найден и имеет правильные значения
        assertThat(mpaRatingOptional)
                .isPresent()
                .hasValueSatisfying(mpaRating ->
                        assertThat(mpaRating)
                                .hasFieldOrPropertyWithValue("id", 1L)
                                .hasFieldOrProperty("name"));
    }

    @Test
    public void testContainsMpaRating() {
        // Проверяем существование рейтинга с ID 1
        boolean exists = mpaRatingStorage.contains(1L);

        // Ожидаем, что рейтинг существует
        assertThat(exists).isTrue();

        // Проверяем отсутствие рейтинга с несуществующим ID
        exists = mpaRatingStorage.contains(999L);
        assertThat(exists).isFalse();
    }
}
