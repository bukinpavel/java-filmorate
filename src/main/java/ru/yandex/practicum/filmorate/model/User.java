package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.Set;

@Data
public class User {
    private Integer id;
    @NotBlank(message = "Электронная почта не может быть пустой.")
    @NotNull(message = "Электронная почта не может быть null.")
    @Email(message = "Электронная почта должна быть правильно оформлена.")
    private final String email;
    @NotBlank(message = "логин не может быть пустым и содержать пробелы")
    private final String login;
    private String name;
    @PastOrPresent(message = "дата рождения не может быть в будущем")
    private final LocalDate birthday;
    private Set<Long> friendsId;

    public Set<Long> getFriends() {
        return friendsId;
    }

}
