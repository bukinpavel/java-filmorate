package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private int id = 1;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public FilmStorage getFilmStorage() {
        return filmStorage;
    }

    public List<Film> findAll() {
        List<Film> values = new ArrayList<>(filmStorage
                .getFilms().values());
        return values;
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
        if (film.getId() == null) {
            film.setId(id);
            id++;
        }
        filmStorage.getFilms().put(film.getId(), film);
        return film;
    }

    public Film put(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getId() == null) {
            film.setId(id);
            id++;
        }
        if (!filmStorage.getFilms().containsKey(film.getId())) {
            throw new ValidationException("Объекта с таким ID нет.");
        }
        filmStorage.getFilms().put(film.getId(), film);
        return film;
    }

    public void setLikeToFilm(Long id, Long userId) {
        if (!filmStorage.getFilms().get(id).getLikes().contains(userId)) {
            filmStorage.getFilms().get(id).getLikes().add(userId);
        }
    }

    public void deleteLikeToFilm(Long id, Long userId) {
        if (filmStorage.getFilms().get(id).getLikes().contains(userId)) {
            filmStorage.getFilms().get(id).getLikes().remove(userId);
        }
    }

    public List<Film> showPopularFilms(){
        List<Film> sortedList = filmStorage.getFilms().values().stream()
                .sorted(Comparator.comparing(Film:: getLikeSize, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
        /*
        if(sortedList.get(0).getLikes() == null || sortedList.get(0).getLikeSize() == 0){
            sortedList = sortedList.subList(0,10);
        }

         */
        return sortedList;
    }

}
