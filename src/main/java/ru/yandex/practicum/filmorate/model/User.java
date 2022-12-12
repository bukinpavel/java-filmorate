package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Data
public class User {
    private Integer id;
    private final String email;
    private final String login;
    private String name;
    private final LocalDate birthday;


}
