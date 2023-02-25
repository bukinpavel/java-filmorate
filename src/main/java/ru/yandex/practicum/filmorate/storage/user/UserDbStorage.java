package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;
import java.util.*;

@Component
@Qualifier("UserDbStorage")
public class UserDbStorage implements UserStorage {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findById(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", id);
        SqlRowSet userFriendsRows = jdbcTemplate.queryForRowSet(
                "SELECT USERS.ID, USER_FRIENDS.FRIEND_ID FROM USERS LEFT JOIN USER_FRIENDS ON USERS.ID = USER_FRIENDS.USER_ID  WHERE USER_ID=?", id);
        SqlRowSet userFriendsRequestRows = jdbcTemplate.queryForRowSet(
                "SELECT USERS.ID, USER_REQUEST.FRIEND_ID FROM USERS LEFT JOIN USER_REQUEST ON USERS.ID = USER_REQUEST.USER_ID  WHERE USER_ID=?", id);
        if (userRows.next()) {
            log.info("Найден user: {} {}", userRows.getString("id"), userRows.getString("login"));
            User user = new User(
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getTimestamp("birthday").toLocalDateTime().toLocalDate()
            );
            user.setName(userRows.getString("name"));
            user.setId(userRows.getInt("id"));
            log.info("Найден пользователь: {} {}", user.getId(), user.getName());
            while (userFriendsRows.next()) {
                user.getFriendsId().add(userFriendsRows.getInt("friend_id"));

            }
            while (userFriendsRequestRows.next()) {
                user.getFriendRequest().add(userFriendsRequestRows.getInt("friend_id"));

            }
            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public Map<Integer, User> getUsers() {
        Map<Integer, User> users = new HashMap<>();
        int id = -1;
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users");
        while (userRows.next()) {
            id = userRows.getInt("id");
            users.put(id, findById(id).get());
        }
        return users;
    }

    @Override
    public void addUser(User user) {
        String sqlQuery = "insert into users(email, login, name, birthday) " +
                "values (?, ?, ?,?)";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
    }

    public void update(User user) {
        String sqlQuery = "update users set " +
                "email = ?, login = ?, name = ?, birthday = ? " +
                "where id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
    }

    /*
     @Override
     public void addFriend(User user, User otherUser) {
         String sqlQuery = "insert into user_friends(user_id, friend_id) " +
                 "values (?,?)";
         jdbcTemplate.update(sqlQuery,
                 user.getId(),
                 otherUser.getId());
     }

     */
    @Override
    public void addFriend(Integer user, Integer otherUser) {
        String sqlQuery = "insert into user_friends(user_id, friend_id) " +
                "values (?,?)";
        jdbcTemplate.update(sqlQuery,
                user,
                otherUser);
    }

    @Override
    public boolean deleteUser(Integer id) {
        String sqlQuery = "delete from users where id = ?";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    @Override
    public boolean deleteFriend(Integer userId, Integer friendId) {
        String sqlQuery = "delete from user_friends where friend_id =? and id = ?";
        return jdbcTemplate.update(sqlQuery, userId, friendId) > 0;
    }

    @Override
    public List<User> getFriendList(Integer userId) {
        List<User> friendList = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select friend_id from user_friends where user_id = ?", userId);
        while (userRows.next()) {
            int id = userRows.getInt("friend_id");
            friendList.add(findById(id).get());
        }
        return friendList;
    }

    @Override
    public List<User> getCommonFriendList(Integer id, Integer otherId) {
        List<User> mutualFriends = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "select FRIEND_ID from USER_FRIENDS WHERE USER_ID = ? INTERSECT SELECT FRIEND_ID from USER_FRIENDS WHERE USER_ID =?"
                , id, otherId);
        while (userRows.next()) {
            int mutualFriendId = userRows.getInt("friend_id");
            mutualFriends.add(findById(mutualFriendId).get());
        }
        return mutualFriends;
    }
}

