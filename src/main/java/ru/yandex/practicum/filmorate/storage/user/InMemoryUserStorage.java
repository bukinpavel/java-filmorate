package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer id = 1;

    public Map<Integer, User> getUsers() {
        return users;
    }
    public void addUser(User user){
        users.put(user.getId(), user);
    }

    public void deleteUser(User user){
        users.remove(user.getId());

    }
    public void modifyUser(User user){

    }
}
