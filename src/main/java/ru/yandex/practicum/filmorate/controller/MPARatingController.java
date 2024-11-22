package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.List;

@RestController
@RequestMapping(path = "/mpa")
@Slf4j
public class MPARatingController {
    private final MpaRatingService mpaRatingService;

    @Autowired
    public MPARatingController(final MpaRatingService mpaRatingService) {
        this.mpaRatingService = mpaRatingService;
    }

    @GetMapping
    public List<MpaRating> getMpaRatings() {
        log.info("Get MPA ratings");
        return mpaRatingService.findAll();
    }

    @GetMapping(path = "/{mpaId}")
    public MpaRating getMPARating(@PathVariable Long mpaId) {
        log.info("Get MPA rating {}", mpaId);
        return mpaRatingService.findById(mpaId);
    }
}
