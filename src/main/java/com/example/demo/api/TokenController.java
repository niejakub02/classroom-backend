package com.example.demo.api;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class TokenController {
    @GetMapping("/token/verify")
    public ResponseEntity<?> verifyToken() {
        return ResponseEntity.ok().build();
    }
}
