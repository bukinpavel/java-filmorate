package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
	private final FilmDbStorage filmStorage;
	@Test
	void contextLoads() {
		/*
		Assertions.assertEquals(6, filmStorage.getGenres().size());
		List<Optional<Genre>> genreList = filmStorage.getGenres();
		System.out.println(genreList);

		Assertions.assertEquals(5, filmStorage.getRatings().size());
		System.out.println(filmStorage.getRatings());

		 */
	}

}
