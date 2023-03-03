package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.sql.PreparedStatement;
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
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id = ?", id);
        SqlRowSet userFriendsRows = jdbcTemplate.queryForRowSet(
                "SELECT user_id, user_friends.friend_id FROM users " +
                        "LEFT JOIN user_friends ON users.id = user_friends.user_id  " +
                        "WHERE user_id=?", id);
        SqlRowSet userFriendsRequestRows = jdbcTemplate.queryForRowSet(
                "SELECT users.id, user_request.friend_id FROM users " +
                        "LEFT JOIN user_request ON user_id = user_request.user_id" +
                        "  WHERE user_id=?", id);
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
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users");
        while (userRows.next()) {
            id = userRows.getInt("id");
            users.put(id, findById(id).get());
        }
        return users;
    }

    @Override
    public void addUser(User user) {
        String sqlQuery = "INSERT INTO users(email, login, name, birthday)" +
                "VALUES (?, ?, ?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setObject(4, user.getBirthday());
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
    }

    public void update(User user) {
        String sqlQuery = "UPDATE users SET " +
                "email = ?, login = ?, name = ?, birthday = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
    }

    @Override
    public void addFriend(Integer user, Integer otherUser) {
        String sqlQuery = "INSERT INTO user_friends(user_id, friend_id) " +
                "VALUES (?,?)";
        jdbcTemplate.update(sqlQuery,
                user,
                otherUser);
    }

    @Override
    public boolean deleteUser(Integer id) {
        String sqlQuery = "DELETE FROM users WHERE id = ?";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    @Override
    public boolean deleteFriend(Integer userId, Integer friendId) {
        String sqlQuery = "DELETE FROM user_friends WHERE user_id =? AND friend_id = ?";
        return jdbcTemplate.update(sqlQuery, userId, friendId) > 0;
    }

    @Override
    public List<User> getFriendList(Integer userId) {
        List<User> friendList = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT friend_id FROM user_friends WHERE user_id = ?", userId);
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
                "SELECT friend_id FROM user_friends WHERE user_id = ? INTERSECT SELECT friend_id from user_friends" +
                        " WHERE user_id =?"
                , id, otherId);
        while (userRows.next()) {
            int mutualFriendId = userRows.getInt("friend_id");
            mutualFriends.add(findById(mutualFriendId).get());
        }
        return mutualFriends;
    }
}

