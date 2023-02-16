package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
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
    private Set<Long> likes = new HashSet<>();;

    public Set<Long> getLikes() {
        return likes;
    }

    public int getLikeSize(){
        return this.getLikes().size();
    }

}
