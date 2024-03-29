package ru.yandex.practicum.filmorate.controller;

import org.assertj.core.error.ShouldBeAfterYear;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

class UserControllerTest {

    @Test
    void put() throws Exception {
        User userWithoutEmail = new User(null, "login", LocalDate.of(1985, 2, 5));
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                putExecutable(userWithoutEmail)
        );
        Assertions.assertEquals("Адрес электронной почты не может быть пустым.", ex.getMessage());

    }

    @Test
    void checkPutOneSizeOne() throws Exception {
        User user = new User("pvb@mail.ru", "login", LocalDate.of(1985, 2, 5));
        UserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserService(userStorage);
        UserController userController = new UserController(userService);
        userController.create(user);
        Assertions.assertEquals(1, userStorage.getUsers().size());
        System.out.println(userStorage.getUsers().values());
        System.out.println(userStorage.getUsers().keySet());

    }

    private Executable generateExecutable(User user) {
        UserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserService(userStorage);
        UserController userController = new UserController(userService);
        return () -> userController.create(user);
    }

    private Executable putExecutable(User user) {
        UserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserService(userStorage);
        UserController userController = new UserController(userService);
        return () -> userController.put(user);
    }


}



