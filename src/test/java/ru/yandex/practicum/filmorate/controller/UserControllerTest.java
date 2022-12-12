package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import exception.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;


import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //@MockBean
    //private RegisterUserCase registerUserCase;

    @Test
    void findAll() throws Exception {

    }

    @Test
    void testCreate() throws Exception {
        User userWithoutEmail = new User(0, null, "login", "name", LocalDate.of(1985, 2, 5));
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                generateExecutable(userWithoutEmail)
        );
        Assertions.assertEquals("Адрес электронной почты не может быть пустым.", ex.getMessage());

        User userWithoutLogin = new User(0, null, "", "name", LocalDate.of(1985, 2, 5));
        ValidationException exLog = Assertions.assertThrows(
                ValidationException.class,
                generateExecutable(userWithoutEmail)
        );
        Assertions.assertEquals("Адрес электронной почты не может быть пустым.", exLog.getMessage());
    }


    @Test
    void put() throws Exception {
        User userWithoutEmail = new User(0, null, "login", "name", LocalDate.of(1985, 2, 5));
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                putExecutable(userWithoutEmail)
        );
        Assertions.assertEquals("Адрес электронной почты не может быть пустым.", ex.getMessage());

    }

    private Executable generateExecutable(User user) {
        UserController userController = new UserController();
        return () -> userController.create(user);
    }
    private Executable putExecutable(User user) {
        UserController userController = new UserController();
        return () -> userController.put(user);
    }
}



