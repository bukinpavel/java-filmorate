package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Data
public class Film {
    private final Integer id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final long duration;
}
