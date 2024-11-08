package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.MpaRatingNotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.util.List;
import java.util.Optional;

@Service
public class MpaRatingService {
    private MpaRatingStorage mpaRatingStorage;

    @Autowired
    public MpaRatingService(MpaRatingStorage mpaRatingStorage) {
        this.mpaRatingStorage = mpaRatingStorage;
    }

    public List<MpaRating> findAll() {
        return mpaRatingStorage.findAll();
    }

    public MpaRating findById(long mpaRatingId) {
        Optional<MpaRating> optionalMpaRating = mpaRatingStorage.findById(mpaRatingId);
        return optionalMpaRating.orElseThrow(() -> new MpaRatingNotFoundException(mpaRatingId));
    }

    public boolean contains(long mpaRatingId) {
        return mpaRatingStorage.contains(mpaRatingId);
    }
}
