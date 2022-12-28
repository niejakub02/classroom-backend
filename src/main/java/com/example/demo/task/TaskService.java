package com.example.demo.task;

import com.example.demo.appUser.AppUser;
import com.example.demo.appUser.AppUserRepo;
import com.example.demo.course.Course;
import com.example.demo.course.CourseRepo;
import com.example.demo.forms.TaskForm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class TaskService {
    TaskRepo taskRepo;
    AppUserRepo appUserRepo;
    CourseRepo courseRepo;

    public List<Task> getTask(Long id) {
        Optional<Task> optionalTask = taskRepo.findById(id);
        if (!optionalTask.isPresent()) {
            return null;
        }
        List<Task> tasks = new ArrayList<>();
        tasks.add(optionalTask.get());
        return tasks;
    }

    public List<Task> getTasks() {
        return taskRepo.findAll();
    }

    public Task saveTask(TaskForm taskForm, Long courseId, Authentication authentication) {
        Optional<AppUser> currentUser = appUserRepo.findByUsername(authentication.getPrincipal().toString());
        Optional<Course> course = courseRepo.findById(courseId);

        if (!currentUser.isPresent() || !course.isPresent()) {
            return null;
        }

        if (currentUser.get() != course.get().getTutor()) {
            log.info("You are not the tutor of the course!");
            return null;
        }


        Task task = new Task(taskForm, course.get());
        taskRepo.save(task);
        return task;
    }
}
