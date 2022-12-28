package com.example.demo.api;

import com.example.demo.answer.Answer;
import com.example.demo.answer.AnswerService;
import com.example.demo.course.Course;
import com.example.demo.course.CourseService;
import com.example.demo.forms.*;
import com.example.demo.task.Task;
import com.example.demo.task.TaskService;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class CourseController {
    private final CourseService courseService;
    private final TaskService taskService;
    private final AnswerService answerService;

    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getCourses(@RequestParam(required = false, name = "id") Optional<Long> id) {
        if (id.isPresent()) {
            return ResponseEntity.ok().body(courseService.getCourse(id.get()));
        }
        return ResponseEntity.ok().body(courseService.getCourses());
    }

    @PostMapping("/courses")
    public ResponseEntity<Course> saveCourse(@RequestBody CourseForm course, Authentication authentication) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/courses").toUriString());
        return ResponseEntity.created(uri).body(courseService.saveCourse(course, authentication));
    }

    @GetMapping("/courses/notEnrolled")
    public ResponseEntity<List<Course>> getUsersNotEnrolledCourses(Authentication authentication) {
        return ResponseEntity.ok().body(courseService.getUsersNotEnrolledCourses(authentication));
    }

    @GetMapping("/courses/enrolled")
    public ResponseEntity<List<Course>> getUsersEnrolledCourses(Authentication authentication) {
        return ResponseEntity.ok().body(courseService.getUsersEnrolledCourses(authentication));
    }

    @GetMapping("/courses/tutored")
    public ResponseEntity<List<Course>> getUsersTutoredCourses(Authentication authentication) {
        return ResponseEntity.ok().body(courseService.getUsersTutoredCourses(authentication));
    }

    @GetMapping("/courses/{courseId}/enroll")
    public ResponseEntity<?> enrollCourse(@PathVariable Long courseId, Authentication authentication) {
        courseService.enrollCourse(courseId, authentication);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/courses/{courseId}/userDetails")
    public ResponseEntity<UserDetailsForm> isTutor(@PathVariable Long courseId, Authentication authentication) {
        return ResponseEntity.ok().body(courseService.isTutor(courseId, authentication));
    }

    @GetMapping("/courses/{courseId}/isEnrolled")
    public ResponseEntity<Boolean> isEnrolled(@PathVariable Long courseId, Authentication authentication) {
        return ResponseEntity.ok().body(courseService.isEnrolled(courseId, authentication));
    }

    @PostMapping("/courses/{courseId}/manageEnrollment")
    public ResponseEntity<?> manageEnrollment(@RequestBody ManageEnrollment manageEnrollment, @PathVariable Long courseId, Authentication authentication) {
        courseService.manageEnrollment(manageEnrollment, courseId, authentication);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/courses/{courseId}/kickUser")
    public ResponseEntity<?> kickUser(@RequestBody UserToKick userToKick, @PathVariable Long courseId, Authentication authentication) {
        courseService.kickUser(userToKick, courseId, authentication);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/courses/{courseId}/leaveCourse")
    public ResponseEntity<?> leaveCourse(@PathVariable Long courseId, Authentication authentication) {
        courseService.leaveCourse(courseId, authentication);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/courses/{courseId}/deleteCourse")
    public ResponseEntity<?> deleteCourse(@PathVariable Long courseId, Authentication authentication) {
        courseService.deleteCourse(courseId, authentication);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/answers/{taskId}/canAnswer")
    public ResponseEntity<Boolean> canAnswer(@PathVariable Long taskId, Authentication authentication) {
        return ResponseEntity.ok().body(courseService.canAnswer(taskId, authentication));
    }

//    @GetMapping("/task")
//    public ResponseEntity<List<Task>> getTask(@RequestParam(required = false, name = "id") Optional<Long> id) {
//        if (id.isPresent()) {
//            return ResponseEntity.ok().body(taskService.getTask(id.get()));
//        }
//        return ResponseEntity.ok().body(taskService.getTasks());
//    }

    @GetMapping("/answers/{taskId}")
    public ResponseEntity<List<Answer>> getAnswer(@PathVariable Long taskId, Authentication authentication) {
        return ResponseEntity.ok().body(answerService.getAnswer(taskId, authentication));
    }

    @PostMapping("/courses/{courseId}/addTask")
    public ResponseEntity<Task> saveTask(@RequestBody TaskForm taskForm, @PathVariable Long courseId, Authentication authentication) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/courses/addTask").toUriString());
        return ResponseEntity.created(uri).body(taskService.saveTask(taskForm, courseId, authentication));
    }

    @GetMapping("/courses/byTask/{taskId}")
    public ResponseEntity<Course> getCourseByTask(@PathVariable Long taskId, Authentication authentication) {
        return ResponseEntity.ok().body(courseService.getCourseByTask(taskId, authentication));
    }

    @PutMapping("/answers/{answerId}")
    public ResponseEntity<?> editAnswer(@RequestBody EditAnswerForm editAnswerForm, @PathVariable Long answerId, Authentication authentication) {
        answerService.editAnswer(editAnswerForm, answerId, authentication);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/answers/{answerId}")
    public ResponseEntity<?> deleteAnswer(@PathVariable Long answerId, Authentication authentication) {
        answerService.deleteAnswer(answerId, authentication);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/answers/{answerId}/addScore")
    public ResponseEntity<?> addScoreToAnswer(@RequestBody AddScoreForm addScoreForm, @PathVariable Long answerId, Authentication authentication) {
        answerService.addScoreToAnswer(addScoreForm, answerId, authentication);
        return ResponseEntity.ok().build();
    }
}

