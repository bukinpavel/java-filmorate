package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    /*
    @GetMapping("/{id}")
    public List<Film> findAll(@) {
        return filmService.findAll();
    }

     */

    @PostMapping
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        filmService.create(film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Film> put(@Valid @RequestBody Film film) {
        filmService.put(film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<String> setFilmLikes(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        filmService.setLikeToFilm(id, userId);
        return new ResponseEntity<>("film with " + id + "liked by user " + userId, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<String> deleteLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId){
        filmService.deleteLikeToFilm(id, userId);
        return new ResponseEntity<>("film with " + id + "DISliked by user " + userId, HttpStatus.OK);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> countPopularFilms(@RequestParam(required = false) Integer count){
        List<Film> popularFilms = null;
        if(count!=null) {
             popularFilms = filmService.showPopularFilms().subList(0,count);
        }
        else  popularFilms  = filmService.showPopularFilms().subList(0,10);

        return new ResponseEntity<>(popularFilms, HttpStatus.OK);
    }

}
