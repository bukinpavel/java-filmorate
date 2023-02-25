package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Qualifier("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    @Override
    public Map<Integer, User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public boolean deleteUser(Integer id) {
        users.remove(id);
        return true;
    }
    @Override
    public Optional<User> findById(Integer id) {
        return Optional.of(getUsers().get(id));
    }

    public void update(User user){
        users.put(user.getId(), user);
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
        users.get(id).getFriendsId().add(friendId);
    }

    @Override
    public boolean deleteFriend(Integer userId, Integer friendId) {
        getUsers().get(userId).getFriendsId().remove(friendId);
        return true;
    }

    @Override
    public List<User> getFriendList(Integer userId) {
        List<User> friendList = new ArrayList<>();
        for (Integer friendId : getUsers().get(userId).getFriendsId()) {
            friendList.add(getUsers().get(friendId));
        }
        return friendList;
    }

    @Override
    public List<User> getCommonFriendList(Integer id, Integer otherId) {
        Set<Integer> mutualFriends = new HashSet<>(getUsers().get(id).getFriendsId());
        mutualFriends.retainAll(getUsers().get(otherId).getFriendsId());

        List<User> mutualFriendsList = new ArrayList<>();
        for (Integer friendId : mutualFriends) {
            mutualFriendsList.add(getUsers().get(friendId));
        }
        return mutualFriendsList;
    }
}

