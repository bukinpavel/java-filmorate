package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserStorage {
    Map<Integer, User> getUsers();
    void addUser(User user);
    boolean deleteUser(Integer id);
    Optional<User> findById(Integer id);
    public void update(User user);
    public void addFriend(Integer id, Integer friendId);
    public boolean deleteFriend(Integer userId, Integer friendId);
    public List<User> getFriendList(Integer userId);
    public List<User> getCommonFriendList(Integer id, Integer otherId);
}
