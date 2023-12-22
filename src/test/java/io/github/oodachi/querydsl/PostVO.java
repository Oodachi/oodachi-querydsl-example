package io.github.oodachi.querydsl;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PostVO {

    private String title;
    private String content;
    private LocalDate date;
    private LocalDateTime timestamp;
}
