package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FilmStorage {
    Map<Integer, Film> getFilms();
    void addFilm(Film film);
    boolean deleteFilm(Integer id);
    void modifyFilm(Film film);

    Optional<Film> findById(Integer id);
    public List<Film> findAll();

    void setLike(Integer filmId, Integer userId);
    boolean deleteLike(Integer filmId, Integer userId);
    public Optional<Genre> getGenreById(Integer id);
    public Map<Integer, Genre> getGenres();
    public Optional<Rating> getRatingById(Integer id);
    public Map<Integer, Rating> getRatings();

}

