package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
    @Getter private Set<Integer> likes = new HashSet<>();
    @Getter @Setter
    private Rating mpa;
    @Getter @Setter private Long rate;
    @Getter private List<Genre> genres = new ArrayList<>();


    public int getLikeSize(){
        return getLikes().size();
    }
}

