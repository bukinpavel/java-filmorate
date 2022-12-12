package ru.yandex.practicum.filmorate.controller;

import exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

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
    private final Map<String, User> users = new HashMap<>();
    Integer id = 0;

    @GetMapping
    public List<User> findAll() {
        List<User> values = new ArrayList<>(users.values());
        log.debug("Текущее количество постов: {}", users.size());
        return values;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if(user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        }

        if(!user.getEmail().contains("@")){
            throw new ValidationException("Адрес электронной почты должен содержать @.");
        }
        if(user.getLogin()==null || user.getLogin().isBlank() || user.getLogin().contains(" ")){
            throw new ValidationException("Login не должен быть пустым или содержать пробелы.");
        }
        /*
        if(users.containsKey(user.getEmail())) {
            throw new ValidationException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }
         */
        if(!user.getBirthday().isBefore(LocalDate.now(ZoneId.systemDefault()))){
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        users.put(user.getEmail(), user);
        if(user.getName()==null || user.getName().isBlank()){
            user.setName(user.getLogin());
        }
        if(user.getId()==null){
            user.setId(id);
            id++;
        }
        return user;
    }
    @PutMapping
    public User put(@RequestBody User user) {
        if(user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        }
        if(!user.getEmail().contains("@")){
            throw new ValidationException("Адрес электронной почты должен содержать @.");
        }
        if(user.getLogin()==null || user.getLogin().isBlank() || user.getLogin().contains(" ")){
            throw new ValidationException("Login не должен быть пустым или содержать пробелы.");
        }
        if(!user.getBirthday().isBefore(LocalDate.now(ZoneId.systemDefault()))){
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        /*
        if(!users.containsKey(user.getEmail())){
            throw new ValidationException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }
         */
        users.put(user.getEmail(), user);
        if(user.getName()==null || user.getName().isBlank()){
            user.setName(user.getLogin());
        }
        if(user.getId()==null){
            user.setId(id);
            id++;
        }
        return user;
    }
}
