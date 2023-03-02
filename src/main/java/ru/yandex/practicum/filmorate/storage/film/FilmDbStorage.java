package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.PreparedStatement;
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
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "SELECT * FROM films WHERE id = ?", id);
        SqlRowSet userLikesRows = jdbcTemplate.queryForRowSet(
                "SELECT * FROM users_like WHERE film_id=?", id);
        SqlRowSet genresRows = jdbcTemplate.queryForRowSet(
                "SELECT * FROM film_genre " +
                        "INNER JOIN genres g on film_genre.genres_id = g.id " +
                        "WHERE film_id = ?", id);
        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet(
                "SELECT * FROM film_rating " +
                        "INNER JOIN ratings r on film_rating.ratings_id = r.id " +
                        "WHERE film_id =?", id);

        if (filmRows.next()) {
            log.info("Найден user: {} {}", filmRows.getString("id"), filmRows.getString("name"));
            Film film = new Film(
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getTimestamp("release_date").toLocalDateTime().toLocalDate(),
                    filmRows.getLong("duration")
            );

            film.setId(filmRows.getInt("id"));
            log.info("Найден фильм: {} {}", film.getId(), film.getName());

            while (userLikesRows.next()) {
                film.getLikes().add(userLikesRows.getInt("user_id"));
            }
            while (genresRows.next()) {
                Genre genre = new Genre();
                genre.setId(genresRows.getInt("genres_id"));
                genre.setName(genresRows.getString("name"));
                film.getGenres().add(genre);
            }

            if (ratingRows.next()) {
                film.setMpa(new Rating());
                film.getMpa().setId(ratingRows.getInt("ratings_id"));
                film.getMpa().setName(ratingRows.getString("name"));
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
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films");
        while (filmRows.next()) {
            int id = filmRows.getInt("id");
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
        String sqlQuery = "INSERT INTO users_like(user_id, film_id) " +
                "VALUES (?,?)";
        jdbcTemplate.update(sqlQuery,
                userId,
                filmId);
    }

    @Override
    public boolean deleteLike(Integer filmId, Integer userId) {
        String sqlQuery = "DELETE FROM users_like WHERE user_id =? AND film_id = ?";
        return jdbcTemplate.update(sqlQuery, userId, filmId) > 0;
    }

    @Override
    public void addFilm(Film film) {
        String sqlQuery = "INSERT INTO films(name, description, release_date, duration) " +
                "VALUES (?, ?, ?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setObject(3, film.getReleaseDate());
            stmt.setLong(4, film.getDuration());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());

        String ratingSqlQuery = "INSERT INTO film_rating(ratings_id, film_id)" +
                "VALUES (?,?)";
        jdbcTemplate.update(ratingSqlQuery,
                film.getMpa().getId(),
                keyHolder.getKey().intValue()
        );

        for (Genre e : film.getGenres()) {
            String genreSqlQuery = "INSERT INTO film_genre(genres_id, film_id)" +
                    "VALUES (?,?)";
            jdbcTemplate.update(genreSqlQuery,
                    e.getId(),
                    keyHolder.getKey().intValue()
            );
        }
    }

    @Override
    public boolean deleteFilm(Integer id) {
        String sqlQuery = "DELETE FROM films WHERE id = ?";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    @Override
    public void modifyFilm(Film film) {
        String sqlQuery = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, duration = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId());

        String sqlQueryMpa = "UPDATE film_rating SET " +
                "ratings_id = ? " +
                "WHERE film_id = ?";

        jdbcTemplate.update(sqlQueryMpa,
                film.getMpa().getId(),
                film.getId());

        if (film.getGenres().isEmpty()) {
            String delsqlQuery = "DELETE FROM film_genre WHERE film_id = ?";
            jdbcTemplate.update(delsqlQuery, film.getId());
        } else {
            String delsqlQuery = "DELETE FROM film_genre WHERE film_id = ?";
            jdbcTemplate.update(delsqlQuery, film.getId());
            for (Genre e : film.getGenres()) {
                String genreSqlQuery = "INSERT INTO film_genre(genres_id, film_id)" +
                        "VALUES (?,?)";
                jdbcTemplate.update(genreSqlQuery,
                        e.getId(),
                        film.getId()
                );
                film.getGenres().add(e);
            }
        }
    }

    @Override
    public Optional<Genre> getGenreById(Integer id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(
                "SELECT * FROM genres WHERE id = ?", id);
        if (genreRows.next()) {
            Genre genre = new Genre();
            genre.setId(genreRows.getInt("id"));
            genre.setName(genreRows.getString("name"));
            log.info("Найден genre: {} {}", genreRows.getString("id"), genreRows.getString("name"));
            return Optional.of(genre);
        } else {
            log.info("genre с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Optional<Genre>> getGenres() {
        List<Optional<Genre>> genres = new ArrayList<>();
        SqlRowSet rRows = jdbcTemplate.queryForRowSet("SELECT * FROM genres");
        while (rRows.next()) {
            int id = rRows.getInt("id");
            genres.add(getGenreById(id));
        }
        return genres;
    }

    @Override
    public Optional<Rating> getRatingById(Integer id) {
        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet(
                "SELECT * FROM ratings WHERE id = ?", id);
        if (ratingRows.next()) {
            Rating rating1 = new Rating(
            );
            rating1.setId(ratingRows.getInt("id"));
            rating1.setName(ratingRows.getString("name"));
            log.info("Найден rating: {} {}", ratingRows.getString("id"), ratingRows.getString("name"));
            return Optional.of(rating1);
        } else {
            log.info("rating с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Optional<Rating>> getRatings() {
        List<Optional<Rating>> ratings = new ArrayList<>();
        SqlRowSet rRows = jdbcTemplate.queryForRowSet("SELECT * FROM ratings");
        while (rRows.next()) {
            int id = rRows.getInt("id");
            ratings.add(getRatingById(id));
        }
        return ratings;
    }
}
