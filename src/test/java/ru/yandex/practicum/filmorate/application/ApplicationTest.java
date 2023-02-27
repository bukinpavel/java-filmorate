package ru.yandex.practicum.filmorate.application;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;

    @Test
    public void testFindUserById() {

        Optional<User> userOptional = userStorage.findById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
        System.out.println(userOptional);
        System.out.println();
    }
/*
    @Test
    public void testGetUsers() {

        Map<Integer, User> users = userStorage.getUsers();
        Assertions.assertEquals(3, users.values().size());
        System.out.println(users.values());
    }

    @Test
    public void addFriends() {

        userStorage.addFriend(3, 1);
        Assertions.assertEquals(1, userStorage.findById(3).get().getFriendsId().size());
        System.out.println(userStorage.findById(3).get());
    }

    @Test
    public void removeFriend() {

        userStorage.deleteFriend(3, 1);
        Assertions.assertEquals(0, userStorage.findById(3).get().getFriendsId().size());
        System.out.println(userStorage.findById(3).get());
    }

    @Test
    public void getFriendList() {

        List<User> testList = userStorage.getFriendList( 1);
        Assertions.assertEquals(2, testList.size());
        System.out.println(testList);
    }

    @Test
    public void getMutualFriendList() {
        List<User> testList = userStorage.getCommonFriendList( 1,2);
        Assertions.assertEquals(1, testList.size());
        System.out.println(testList);
    }

    @Test
    public void testFindFilmById() {
        Optional<Film> filmOptional = filmStorage.findById(1);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
        System.out.println(filmOptional);
    }

    @Test
    public void testFindAllFilms() {
        Map<Integer, Film> films = filmStorage.getFilms();
        Assertions.assertEquals(3, films.values().size());
        System.out.println(films.values());

        List<Film> filmsL = filmStorage.findAll();
        Assertions.assertEquals(3, filmsL.size());
        System.out.println(films);
    }

 */
@Test
public void testAddFilms() {
    Film film = new Film(
            "nisi eiusmod",
            "adipisicing",
             LocalDate.of(1967,03,25),
            100
    );
    film.setMpa(new Rating());
    film.getMpa().setId(1);
    filmStorage.addFilm(film);
    //Map<Integer, Film> films = filmStorage.getFilms();
    //filmStorage.findById(1)
    Assertions.assertEquals(1, filmStorage.findById(1).get().getId());
    System.out.println(filmStorage.findById(1).get());
}
}
