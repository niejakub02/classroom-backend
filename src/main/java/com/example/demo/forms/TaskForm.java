package com.example.demo.forms;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskForm {
    private String title;
    private String content;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
}
