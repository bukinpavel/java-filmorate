package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

class FilmControllerTest {

    @Test
    void create() {
        Film filmWithoutName = new Film(null, "RomanticFilm", LocalDate.of(1985, 2, 5),
                70);
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                generateExecutable(filmWithoutName)
        );
        Assertions.assertEquals("Название фильма не может быть пустым.", ex.getMessage());

        Film filmWithOutdateRelease = new Film("Diamond", "RomanticFilm", LocalDate.of(1785, 2, 5), 70);
        ValidationException exOutRelease = Assertions.assertThrows(
                ValidationException.class,
                generateExecutable(filmWithOutdateRelease)
        );
        Assertions.assertEquals("Дата релиза — не раньше 28 декабря 1895 года.", exOutRelease.getMessage());

        Film filmWithNegativeDuration = new Film( "Diamond", "RomanticFilm", LocalDate.of(2020, 2, 5), -70);
        ValidationException exNegDur = Assertions.assertThrows(
                ValidationException.class,
                generateExecutable(filmWithNegativeDuration)
        );
        Assertions.assertEquals("Продолжительность фильма должна быть положительной.",exNegDur.getMessage());

    }

    private Executable generateExecutable(Film film) {
        FilmStorage filmStorage = new InMemoryFilmStorage();
        FilmService filmService = new FilmService(filmStorage);
        FilmController filmController = new FilmController(filmService);
        return () -> filmController.create(film);
    }


    @Test
    void findAll(){
        Film film1 = new Film("ABC", "RomanticFilm", LocalDate.of(1985, 2, 5), 70);
        FilmStorage filmStorage = new InMemoryFilmStorage();
        FilmService filmService = new FilmService(filmStorage);
        FilmController filmController = new FilmController(filmService);
        filmStorage.addFilm(film1);
        Assertions.assertEquals(1, filmController.findAll().size());
        //return filmController.findAll();
    }

    @Test
    void checkFilmLikes(){
        Film film1 = new Film("ABC", "RomanticFilm", LocalDate.of(1985, 2, 5),
                70);
        Film film2 = new Film("DFG", "HorrorFilm", LocalDate.of(1985, 2, 5),
                70);
        Film film3 = new Film("HJK", "ComedyFilm", LocalDate.of(1985, 2, 5),
                70);
        User user1 = new User("pvb@mail.ru", "login",  LocalDate.of(1985, 2, 5));
        User user2 = new User("ABC@mail.ru", "login",  LocalDate.of(1983, 2, 5));
        User user3 = new User("DFG@mail.ru", "login",  LocalDate.of(1980, 2, 5));
        UserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserService(userStorage);
        UserController userController = new UserController(userService);
        userController.create(user1);
        userController.create(user2);
        userController.create(user3);
        FilmStorage filmStorage = new InMemoryFilmStorage();
        FilmService filmService = new FilmService(filmStorage);
        FilmController filmController = new FilmController(filmService);
        filmController.create(film1);
        filmController.create(film2);
        filmController.create(film3);
        Assertions.assertEquals(3, filmService.getFilmStorage().getFilms().values().size());
        filmController.setFilmLikes(1,1);
        filmController.setFilmLikes(1,2);
        filmController.setFilmLikes(1,3);
        filmController.setFilmLikes(2,4);
        Assertions.assertEquals(3, filmService.getFilmStorage().getFilms().get(1).getLikes().size());
        System.out.println(filmService.getFilmStorage().getFilms().values());
        System.out.println(filmController.countPopularFilms(2));
        //return filmController.findAll();
    }


}