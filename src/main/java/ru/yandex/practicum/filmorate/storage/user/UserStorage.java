package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;
@Component
public interface UserStorage {
    Map<Integer, User> getUsers();
    void addUser(User user);
    void deleteUser(User user);
    void modifyUser(User user);
}
