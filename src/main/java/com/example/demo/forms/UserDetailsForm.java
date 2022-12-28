package com.example.demo.forms;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDetailsForm {
    private Long id;
    private boolean isTutor;
}
