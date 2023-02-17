package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;
public interface FilmStorage {
    Map<Integer, Film> getFilms();
    void addFilm(Film film);
    void deleteFilm(Film film);
    void modifyFilm(int id,Film film);
}
