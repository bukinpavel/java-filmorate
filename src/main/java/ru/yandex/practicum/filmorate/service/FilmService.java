package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private int id = 1;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public FilmStorage getFilmStorage() {
        return filmStorage;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Optional<Film> findById(Integer id) {
        if (filmStorage.findById(id).isEmpty()) {
            throw new NotFoundException("Такого id нет");
        }
        return filmStorage.findById(id);
    }

    public Film create(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания - 200 символов");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года.");
        }
        filmStorage.addFilm(film);
        return film;
    }

    public Film put(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }

        if (!filmStorage.getFilms().containsKey(film.getId())) {
            throw new NotFoundException("Объекта с таким ID нет.");
        }

        filmStorage.modifyFilm(film);
        return film;

    }

    public void setLikeToFilm(Integer id, Integer userId) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new NotFoundException("Объекта с таким ID нет.");
        }
        filmStorage.setLike(id,userId);
    }

    public void deleteLikeToFilm(Integer id, Integer userId) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new NotFoundException("Объекта с таким ID нет.");
        }
        if (userId < 1) {
            throw new NotFoundException("Объекта с таким ID нет.");
        }
        filmStorage.deleteLike(id, userId);

    }

    public List<Film> showPopularFilms() {
        List<Film> sortedList = filmStorage.getFilms().values().stream()
                .sorted(Comparator.comparing(Film::getLikeSize, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
        return sortedList;
    }

    public Genre getGenreById(Integer id){
        if (!filmStorage.getGenres().containsKey(id)) {
            throw new NotFoundException("Объекта с таким ID нет.");
        }
        if (id < 1) {
            throw new NotFoundException("Объекта с таким ID нет.");
        }
        return filmStorage.getGenreById(id).get();
    }

    public Map<Integer, Genre> getGenres(){
        //List<Genre> genres = new ArrayList<>(filmStorage.getGenres().values());
        return filmStorage.getGenres();
    }

    public Rating getRatingById(Integer id){
        if (!filmStorage.getRatings().containsKey(id)) {
            throw new NotFoundException("Объекта с таким ID нет.");
        }
        if (id < 1) {
            throw new NotFoundException("Объекта с таким ID нет.");
        }

        return filmStorage.getRatingById(id).get();
    }

    public  Map<Integer, Rating> getRatings(){
        //List<Rating> ratings = new ArrayList<>(filmStorage.getRatings().values());
        return filmStorage.getRatings();
    }
}
