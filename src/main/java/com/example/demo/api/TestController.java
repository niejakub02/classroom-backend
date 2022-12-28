package com.example.demo.api;

import com.example.demo.answer.Answer;
import com.example.demo.answer.AnswerService;
import com.example.demo.appUser.AppUserService;
import com.example.demo.course.Course;
import com.example.demo.course.CourseRepo;
import com.example.demo.course.CourseService;
import com.example.demo.task.Task;
import com.example.demo.task.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class TestController {
    private final AppUserService appUserService;
    private final CourseService courseService;
    private final TaskService taskService;
    private final AnswerService answerService;

    private final CourseRepo courseRepo;

//    @GetMapping("/courses")
//    public ResponseEntity<List<Course>> getCourses(@RequestParam(required = false, name = "id") Optional<Long> id, Authentication authentication) {
//        if (id.isPresent()) {
//            return ResponseEntity.ok().body(courseService.getCourse(id.get()));
//        }
//
//        return ResponseEntity.ok().body(courseService.getCourses());
//    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasks(@RequestParam(required = false, name = "id") Optional<Long> id, Authentication authentication) {
        if (id.isPresent()) {
            return ResponseEntity.ok().body(taskService.getTask(id.get()));
        }

        return ResponseEntity.ok().body(taskService.getTasks());
    }

//    @GetMapping("/answers")
//    public ResponseEntity<List<Answer>> getAnswer(@RequestParam(required = false, name = "id") Optional<Long> id, Authentication authentication) {
//        if (id.isPresent()) {
//            return ResponseEntity.ok().body(answerService.getAnswer(id.get()));
//        }
//
//        return ResponseEntity.ok().body(answerService.getAnswers());
//    }

//    @GetMapping("/test/{courseId}")
//    public ResponseEntity<Boolean> test(@PathVariable Long courseId, Authentication authentication) {
//        return ResponseEntity.ok().body(appUserService.test(courseId, authentication));
//    }




}
