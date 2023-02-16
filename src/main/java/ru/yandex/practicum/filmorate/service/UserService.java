package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage userStorage;
    int id = 1;

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

    public User findById(Integer id) {
        return userStorage.getUsers().get(id);
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

    public void addFriend(Integer id, Integer friendId) {
        userStorage.getUsers().get(id).getFriendsId().add(friendId);
    }

    public void removeFriend(Long id, Long friendId) {
        userStorage.getUsers().get(id).getFriendsId().remove(friendId);
    }

    public List<User> getFriendList(Integer id) {
        List<User> friendList = new ArrayList<>();
        for (Integer friendId : userStorage.getUsers().get(id).getFriendsId()) {
            friendList.add(userStorage.getUsers().get(friendId));
        }
        return friendList;
    }

    public List<User> getCommonFriendList(Integer id, Integer otherId) {
        Set<Integer> mutualFriends = new HashSet<>(userStorage.getUsers().get(id).getFriendsId());
        mutualFriends.retainAll(userStorage.getUsers().get(otherId).getFriendsId());

        List<User> mutualFriendsList = new ArrayList<>();
        for (Integer friendId : mutualFriends) {
            mutualFriendsList.add(userStorage.getUsers().get(friendId));
        }
        return mutualFriendsList;
    }

}
