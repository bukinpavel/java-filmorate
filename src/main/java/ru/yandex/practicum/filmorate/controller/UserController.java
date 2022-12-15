package ru.yandex.practicum.filmorate.controller;

import exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer id = 1;

    @GetMapping
    public List<User> findAll() {
        List<User> values = new ArrayList<>(users.values());
        log.debug("Текущее количество постов: {}", users.size());
        return values;
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        }

        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Адрес электронной почты должен содержать @.");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Login не должен быть пустым или содержать пробелы.");
        }

        if (!user.getBirthday().isBefore(LocalDate.now(ZoneId.systemDefault()))) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (user.getId() == null) {
            user.setId(id);
            id++;
        }
        users.put(user.getId(), user);
        return new ResponseEntity(user, HttpStatus.ACCEPTED);
    }

    @PutMapping
    public ResponseEntity<User> put(@Valid @RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Адрес электронной почты должен содержать @.");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Login не должен быть пустым или содержать пробелы.");
        }
        if (!user.getBirthday().isBefore(LocalDate.now(ZoneId.systemDefault()))) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Объекта с таким ID нет.");
        }
        users.put(user.getId(), user);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }
}
