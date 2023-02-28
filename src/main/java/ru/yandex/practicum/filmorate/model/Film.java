package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;


@Data
public class Film {
    private Integer id;
    @NotBlank(message = "название не может быть пустым.")
    private final String name;
    @Size(max=200,message = "максимальная длина описания — 200 символов")
    private final String description;
    private final LocalDate releaseDate;
    @Positive(message = "продолжительность фильма должна быть положительной.")
    private final long duration;
    private Set<Integer> likes = new HashSet<>();
    private Rating mpa;
    private Long rate;
    private LinkedHashSet<Genre> genres = new LinkedHashSet<Genre>();


    public int getLikeSize(){
        return getLikes().size();
    }
}

