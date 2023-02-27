package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;
    int id = 1;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }

    public List<User> findAll() {
        List<User> values = new ArrayList<>(userStorage.getUsers().values());
        return values;
    }

    public Optional<User> findById(Integer id) {
        if (userStorage.findById(id).isEmpty()) {
            throw new NotFoundException("Такого id нет");
        }
        return userStorage.findById(id);
    }
    public User create(User user) {

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
        /*
        if (user.getId() == null) {
            user.setId(id);
            id++;
        }

         */
        userStorage.addUser(user);
        return user;
    }

    public User put(User user) {
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

        if (!userStorage.getUsers().containsKey(user.getId())) {
            throw new NotFoundException("Объекта с таким ID нет.");
        }
        userStorage.update(user);
        return user;

    }

    public void addFriend(Integer id, Integer friendId) {
        if(friendId < 1){
            throw new NotFoundException("Объекта с таким ID нет.");
        }
        userStorage.addFriend(id,friendId);
    }

    public void removeFriend(Integer id, Integer friendId) {
        userStorage.deleteFriend(id, friendId);
    }

    public List<User> getFriendList(Integer id) {
        return userStorage.getFriendList(id);
    }

    public List<User> getCommonFriendList(Integer id, Integer otherId) {
        return userStorage.getCommonFriendList(id,otherId);
    }
}
