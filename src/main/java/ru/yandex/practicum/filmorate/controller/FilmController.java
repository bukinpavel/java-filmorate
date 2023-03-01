package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
//@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/films/{id}")
    public ResponseEntity<Film> findById(@PathVariable("id") Integer id) {
        return new ResponseEntity(filmService.findById(id), HttpStatus.OK);
    }


    @PostMapping("/films")
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        filmService.create(film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @PutMapping("/films")
    public ResponseEntity<Film> put(@Valid @RequestBody Film film) {
        filmService.put(film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public ResponseEntity<String> setFilmLikes(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        filmService.setLikeToFilm(id, userId);
        return new ResponseEntity<>("film with " + id + "liked by user " + userId, HttpStatus.OK);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public ResponseEntity<String> deleteLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        filmService.deleteLikeToFilm(id, userId);
        return new ResponseEntity<>("film with " + id + "DISliked by user " + userId, HttpStatus.OK);
    }

    @GetMapping("/films/popular")
    public ResponseEntity<List<Film>> showPopularFilms(@RequestParam(required = false) Integer count) {
        List<Film> popularFilms = filmService.showPopularFilms();
        if (count != null) {
            popularFilms = popularFilms.subList(0, count);
        } else if (filmService.getFilmStorage().getFilms().size() > 10) {
            popularFilms = popularFilms.subList(0, 10);
        } else if (filmService.getFilmStorage().getFilms().size() < 10) {
            popularFilms = popularFilms.subList(0, filmService.getFilmStorage().getFilms().size());
        }
        return new ResponseEntity<>(popularFilms, HttpStatus.OK);
    }

    @GetMapping("/genres")
    public ResponseEntity<List<Genre>> getGenres() {
        return new ResponseEntity(filmService.getGenres(), HttpStatus.OK);
    }

    @GetMapping("/genres/{id}")
    public ResponseEntity<Genre> getGenre(@PathVariable("id") Integer id) {
        return new ResponseEntity(filmService.getGenreById(id), HttpStatus.OK);
    }

    @GetMapping("/mpa")
    public ResponseEntity<List<Rating>> getRatings() {
        return new ResponseEntity(filmService.getRatings(), HttpStatus.OK);
    }

    @GetMapping("/mpa/{id}")
    public ResponseEntity<Rating> getRating(@PathVariable("id") Integer id) {
        return new ResponseEntity(filmService.getRatingById(id), HttpStatus.OK);
    }


}
