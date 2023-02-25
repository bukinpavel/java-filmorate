package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class ChatMessage {
    @NonNull
    private Integer id;
    private String userFrom;
    private String userTo;
    private Date sendDate;
    private String message;
    private Boolean userRead;
}