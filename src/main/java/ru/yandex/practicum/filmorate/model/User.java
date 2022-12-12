package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Data
public class User {
    private final Integer id;
    private final String email;
    private final String login;
    private final String name;
    private final LocalDate birthday;
}
