package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.*;

@Component
@Qualifier("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    public void addFilm(Film film) {
        films.put(film.getId(), film);
    }

    public boolean deleteFilm(Integer id) {
        films.remove(id);
        return true;
    }

    public void modifyFilm(Film film) {
        films.replace(film.getId(), film);
    }

    @Override
    public Optional<Film> findById(Integer id) {
        return Optional.of(getFilms().get(id));
    }

    public Map<Integer, Film> getFilms() {
        return films;
    }

    public List<Film> findAll() {
        List<Film> values = new ArrayList<>(getFilms().values());
        return values;
    }

    @Override
    public void setLike(Integer filmId, Integer userId) {
        getFilms().get(filmId).getLikes().add(userId);
    }

    @Override
    public boolean deleteLike(Integer filmId, Integer userId) {
        getFilms().get(filmId).getLikes().remove(userId);
        return true;
    }

    @Override
    public Optional<Genre> getGenreById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Map<Integer, Genre> getGenres() {
        return null;
    }

    @Override
    public Optional<Rating> getRatingById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Map<Integer, Rating> getRatings() {
        return null;
    }
}
