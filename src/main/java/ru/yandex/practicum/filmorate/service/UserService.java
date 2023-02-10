package ru.yandex.practicum.filmorate.service;

import exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserService {
    private final UserStorage userStorage;
    int id;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }

    public List<User> findAll() {
        List<User> values = new ArrayList<>(userStorage.getUsers().values());
        return values;
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

        if (user.getId() == null) {
            user.setId(id);
            id++;
        }
        userStorage.getUsers().put(user.getId(), user);
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
            throw new ValidationException("Объекта с таким ID нет.");
        }
        userStorage.getUsers().put(user.getId(), user);
        return user;
    }

    public void addFriend(Long id, Long friendId) {
        userStorage.getUsers().get(id).getFriends().add(friendId);
    }

    public void removeFriend(Long id, Long friendId) {
        userStorage.getUsers().get(id).getFriends().remove(friendId);
    }

    public List<User> getFriendList(Long id) {
        List<User> friendList = new ArrayList<>();
        for (Long friendId : userStorage.getUsers().get(id).getFriends()) {
            friendList.add(userStorage.getUsers().get(friendId));
        }
        return friendList;
    }

    public List<User> getCommonFriendList(Long id, Long otherId) {
        Set<Long> mutualFriends = new HashSet<>(userStorage.getUsers().get(id).getFriends());
        mutualFriends.retainAll(userStorage.getUsers().get(otherId).getFriends());

        List<User> mutualFriendsList = new ArrayList<>();
        for (Long friendId : mutualFriends) {
            mutualFriendsList.add(userStorage.getUsers().get(friendId));
        }
        return mutualFriendsList;
    }

}
