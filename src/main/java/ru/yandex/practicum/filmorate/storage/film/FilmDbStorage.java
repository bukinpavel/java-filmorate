package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Film> findById(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films where id = ?", id);
        SqlRowSet userLikesRows = jdbcTemplate.queryForRowSet("select user_id from users_like where film_id=?", id);
        SqlRowSet genresRows = jdbcTemplate.queryForRowSet(
                "SELECT FILM_GENRE.FILM_ID, GENRES.NAME\n" +
                        "FROM FILM_GENRE\n" +
                        "LEFT JOIN GENRES ON FILM_GENRE.GENRES_ID = GENRES.ID\n" +
                        "WHERE FILM_ID = ?", id);
        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet(
                "SELECT FILM_RATING.FILM_ID, RATINGS.NAME\n" +
                        "FROM FILM_RATING\n" +
                        "LEFT JOIN RATINGS ON FILM_RATING.RATINGS_ID = RATINGS.ID\n" +
                        "WHERE FILM_ID =?", id);

        if (filmRows.next()) {
            log.info("Найден user: {} {}", filmRows.getString("id"), filmRows.getString("name"));
            Film film = new Film(
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getTimestamp("release_date").toLocalDateTime().toLocalDate(),
                    filmRows.getInt("duration")
            );
            film.setId(filmRows.getInt("id"));
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            while (userLikesRows.next()) {
                film.getLikes().add(userLikesRows.getInt("user_id"));
            }
            while (genresRows.next()) {
                film.getGenre().add(genresRows.getString("name"));
            }
            while (ratingRows.next()) {
                film.getRating().add(ratingRows.getString("name"));
            }
            return Optional.of(film);
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public Map<Integer, Film> getFilms() {
        Map<Integer, Film> films = new HashMap<>();
        int id = -1;
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films");
        while (filmRows.next()) {
            id = filmRows.getInt("id");
            films.put(id, findById(id).get());
        }
        return films;
    }

    public List<Film> findAll() {
        List<Film> values = new ArrayList<>(getFilms().values());
        return values;
    }

    @Override
    public void setLike(Integer filmId, Integer userId) {
        String sqlQuery = "insert into users_like(user_id, friend_id) " +
                "values (?,?)";
        jdbcTemplate.update(sqlQuery,
                userId,
                filmId);
    }

    @Override
    public boolean deleteLike(Integer filmId, Integer userId) {
        String sqlQuery = "delete from users_like where user_id =? and film_id = ?";
        return jdbcTemplate.update(sqlQuery, userId, filmId) > 0;
    }

    @Override
    public void addFilm(Film film) {
        String sqlQuery = "insert into films(name, description, release_date, durationy) " +
                "values (?, ?, ?,?)";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration());
    }

    @Override
    public boolean deleteFilm(Integer id) {
        String sqlQuery = "delete from users where id = ?";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    @Override
    public void modifyFilm(Film film) {
        String sqlQuery = "update films set " +
                "name = ?, description = ?, release_date = ?, duration = ? " +
                "where id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId());
    }


    @Override
    public Optional<String> getGenreById(Integer id) {
        String genre;
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(
                "select * from genres where id = ?", id);
        if (genreRows.next()) {
            log.info("Найден genre: {} {}", genreRows.getString("id"), genreRows.getString("name"));
            genre = genreRows.getString("name");
            return Optional.of(genre);
        } else {
            log.info("Жанр с идентификатором {} не найден.", id);
            return Optional.empty();

        }
    }

    @Override
    public Map<Integer, String> getGenres() {
        Map<Integer, String> genres = new HashMap<>();
        int id = -1;
        SqlRowSet gRows = jdbcTemplate.queryForRowSet("select * from genres");
        while (gRows.next()) {
            id = gRows.getInt("id");
            genres.put(id, getGenreById(id).get());
        }
        return genres;
    }

    @Override
    public Optional<String> getRatingById(Integer id) {
        String rating;
        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet(
                "select * from ratings where id = ?", id);
        if (ratingRows.next()) {
            log.info("Найден rating: {} {}", ratingRows.getString("id"), ratingRows.getString("name"));
            rating = ratingRows.getString("name");
            return Optional.of(rating);
        } else {
            log.info("rating с идентификатором {} не найден.", id);
            return Optional.empty();

        }
    }

    @Override
    public Map<Integer, String> getRatings() {
        Map<Integer, String> ratings = new HashMap<>();
        int id = -1;
        SqlRowSet rRows = jdbcTemplate.queryForRowSet("select * from ratings");
        while (rRows.next()) {
            id = rRows.getInt("id");
            ratings.put(id, getRatingById(id).get());
        }
        return ratings;
    }
}
