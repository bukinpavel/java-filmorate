package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    public void addFilm(Film film) {
        films.put(film.getId(), film);
    }

    public void deleteFilm(Film film) {
        films.remove(film.getId());
    }

    public void modifyFilm(int id, Film film) {
        films.replace(id, film);
    }

    public Map<Integer, Film> getFilms() {
        return films;
    }
}
