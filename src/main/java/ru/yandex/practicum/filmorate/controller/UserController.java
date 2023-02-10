package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        userService.create(user);
        return new ResponseEntity(user, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> put(@Valid @RequestBody User user) {
        userService.put(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> addFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        userService.addFriend(id, friendId);
        return new ResponseEntity<>("friend with " + friendId + "added to user " + id, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> removeFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        userService.removeFriend(id, friendId);
        return new ResponseEntity<>("friend with " + friendId + "removed from userList " + id, HttpStatus.OK);
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> removeFriend(@PathVariable("id") Long id) {
        userService.getFriendList(id);
        return new ResponseEntity<>(userService.getFriendList(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriendList(@PathVariable("id") Long id,
                                                          @PathVariable("otherId") Long otherId) {
        userService.getCommonFriendList(id, otherId);
        return new ResponseEntity<>(userService.getCommonFriendList(id, otherId), HttpStatus.OK);
    }
}
