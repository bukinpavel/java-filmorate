package ru.yandex.practicum.filmorate.controller;

import exception.ValidationException;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    @Test
    void create() {
        Film filmWithoutName = new Film(0, null, "RomanticFilm", LocalDate.of(1985, 2, 5), 70);
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                generateExecutable(filmWithoutName)
        );
        Assertions.assertEquals("Название фильма не может быть пустым.", ex.getMessage());

        Film filmWithOutdateRelease = new Film(1, "Diamond", "RomanticFilm", LocalDate.of(1785, 2, 5), 70);
        ValidationException exOutRelease = Assertions.assertThrows(
                ValidationException.class,
                generateExecutable(filmWithOutdateRelease)
        );
        Assertions.assertEquals("Дата релиза — не раньше 28 декабря 1895 года.", exOutRelease.getMessage());

        Film filmWithNegativeDuration = new Film(1, "Diamond", "RomanticFilm", LocalDate.of(2020, 2, 5), -70);
        ValidationException exNegDur = Assertions.assertThrows(
                ValidationException.class,
                generateExecutable(filmWithNegativeDuration)
        );
        Assertions.assertEquals("Продолжительность фильма должна быть положительной.",exNegDur.getMessage());

    }
    private Executable generateExecutable(Film film) {
        FilmController filmController = new FilmController();
        return () -> filmController.create(film);
    }
}