package com.example.demo.api;

import com.example.demo.appUser.AppUser;
import com.example.demo.appUser.AppUserRepo;
import com.example.demo.appUser.AppUserService;
import com.example.demo.appUserRole.AppUserRole;
import com.example.demo.course.Course;
import com.example.demo.course.CourseService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getUser(@RequestParam(required = false, name = "username") Optional<String> username) {
        if (username.isPresent()) {
            return ResponseEntity.ok().body(appUserService.getUser(username.get()));
        }
        return ResponseEntity.ok().body(appUserService.getUsers());
    }

    @GetMapping("/users/courses")
    public ResponseEntity<List<Course>> getUsersCourses(Authentication authentication) {
        return ResponseEntity.ok().body(appUserService.getCourses(authentication));
    }

    @PostMapping("/users")
    public ResponseEntity<?> saveUser(@RequestBody AppUser user) {
        try {
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users").toUriString());
            return ResponseEntity.created(uri).body(appUserService.saveUser(user));
        } catch(IllegalStateException illegalStateException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(illegalStateException.getMessage());
        }
    }

//    @PostMapping("/roles/save")
//    public ResponseEntity<AppUserRole> saveRole(@RequestBody AppUserRole role) {
//        return ResponseEntity.ok().body(appUserService.saveRole(role));
//    }
//
//    @PostMapping("/roles/addtouser")
//    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
//        appUserService.addRoleToUser(form.getUsername(), form.getRoleName());
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping("/student/courses")
//    public ResponseEntity<List<Course>> getCourses(Authentication authentication) {
//        return ResponseEntity.ok().body(appUserService.getCourses(authentication));
//    }
}
