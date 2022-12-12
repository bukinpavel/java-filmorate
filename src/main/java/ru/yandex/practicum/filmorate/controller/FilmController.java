package ru.yandex.practicum.filmorate.controller;

import exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<String, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> findAll() {
        List<Film> values = new ArrayList<>(films.values());
        log.debug("Текущее количество фильмов: {}", films.size());
        return values;
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        if(film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if(film.getDescription().length() > 200){
            throw new ValidationException("Максимальная длина описания - 200 символов");
        }
        if(film.getDuration()<0){
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
        if(film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))){
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года.");
        }
        films.put(film.getName(), film);
        return film;
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        if(film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        films.put(film.getName(), film);
        return film;
    }
}
