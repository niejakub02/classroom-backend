package com.example.demo.course;

import com.example.demo.answer.Answer;
import com.example.demo.answer.AnswerRepo;
import com.example.demo.appUser.AppUser;
import com.example.demo.appUser.AppUserRepo;
import com.example.demo.forms.*;
import com.example.demo.task.Task;
import com.example.demo.task.TaskRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class CourseService {
    CourseRepo courseRepo;
    AppUserRepo appUserRepo;
    TaskRepo taskRepo;
    AnswerRepo answerRepo;

    public List<Course> getCourse(Long id) {
        Optional<Course> optionalCourse = courseRepo.findById(id);
        if (!optionalCourse.isPresent()) {
            return null;
        }

        List<Course> courses = new ArrayList<>();
        courses.add(optionalCourse.get());
        return courses;
    }

    public List<Course> getCourses() {
        return courseRepo.findAll();
    }

    public Course saveCourse(CourseForm course, Authentication authentication) {
        Optional<AppUser> currentUser = appUserRepo.findByUsername(authentication.getPrincipal().toString());

        if (!currentUser.isPresent()) {
            return null;
        }

        return courseRepo.save(new Course(course.getTitle(), course.getAbout(), currentUser.get()));
    }

    public List<Course> getUsersEnrolledCourses(Authentication authentication) {
        Optional<AppUser> currentUser = appUserRepo.findByUsername(authentication.getPrincipal().toString());

        if (!currentUser.isPresent()) {
            return null;
        }

        List<Course> enrolledCourses = courseRepo.findAll().stream().filter(course ->
                course.isUserEnrolledAndNotTutor(currentUser.get())
        ).collect(Collectors.toList());
        return enrolledCourses;
    }

    public List<Course> getUsersTutoredCourses(Authentication authentication) {
        Optional<AppUser> currentUser = appUserRepo.findByUsername(authentication.getPrincipal().toString());

        if (!currentUser.isPresent()) {
            return null;
        }

        return courseRepo.findByTutor(currentUser.get());
    }

    public List<Course> getUsersNotEnrolledCourses(Authentication authentication) {
        Optional<AppUser> currentUser = appUserRepo.findByUsername(authentication.getPrincipal().toString());

        if (!currentUser.isPresent()) {
            return null;
        }

        List<Course> courses = courseRepo.findAll();
        List<Course> notEnrolledCourses = courses.stream().filter(course ->
                !course.isUserEnrolledOrPending(currentUser.get())
        ).collect(Collectors.toList());
        return notEnrolledCourses;
    }

    public void enrollCourse(Long courseId, Authentication authentication) {
        Optional<AppUser> currentUser = appUserRepo.findByUsername(authentication.getPrincipal().toString());
        Optional<Course> course = courseRepo.findById(courseId);

        if (!currentUser.isPresent() || !course.isPresent()) {
            return;
        }

        if (course.get().isUserEnrolledOrPending(currentUser.get())) {
            log.info("User already pending or enrolled");
            return;
        }

        course.get().getPendingUsers().add(currentUser.get());

        return;
    }

    public UserDetailsForm isTutor(Long courseId, Authentication authentication) {
        Optional<AppUser> currentUser = appUserRepo.findByUsername(authentication.getPrincipal().toString());
        Optional<Course> course = courseRepo.findById(courseId);

        if (!currentUser.isPresent() || !course.isPresent()) {
            return null;
        }

//        return currentUser.get() == course.get().getTutor();

        return new UserDetailsForm(currentUser.get().getId(), currentUser.get() == course.get().getTutor());
    }

    public void manageEnrollment(ManageEnrollment manageEnrollment, Long courseId, Authentication authentication) {
        Optional<AppUser> currentUser = appUserRepo.findByUsername(authentication.getPrincipal().toString());
        Optional<AppUser> user = appUserRepo.findByUsername(manageEnrollment.getUsername());
        Optional<Course> course = courseRepo.findById(courseId);


        if (!currentUser.isPresent() || !course.isPresent() || !user.isPresent()) {
            return;
        }

        if (currentUser.get() != course.get().getTutor()) {
            log.info("You are not the tutor of the course!");
            return;
        }

        if (manageEnrollment.isDecision()) {
            course.get().getPendingUsers().remove(user.get());
            course.get().getEnrolledUsers().add(user.get());
        } else {
            course.get().getPendingUsers().remove(user.get());
        }

        return;
    }

    public void kickUser(UserToKick userToKick, Long courseId, Authentication authentication) {
        Optional<AppUser> currentUser = appUserRepo.findByUsername(authentication.getPrincipal().toString());
        Optional<AppUser> user = appUserRepo.findByUsername(userToKick.getUsername());
        Optional<Course> course = courseRepo.findById(courseId);

        if (!currentUser.isPresent() || !course.isPresent() || !user.isPresent()) {
            return;
        }

        if (currentUser.get() != course.get().getTutor()) {
            log.info("You are not the tutor of the course!");
            return;
        }

        if (user.get() == course.get().getTutor()) {
            log.info("You can't kick the tutor of the course!");
            return;
        }


        course.get().getEnrolledUsers().remove(user.get());

        return;
    }

    public boolean isEnrolled(Long courseId, Authentication authentication) {
        Optional<AppUser> currentUser = appUserRepo.findByUsername(authentication.getPrincipal().toString());
        Optional<Course> course = courseRepo.findById(courseId);

        if (!currentUser.isPresent() || !course.isPresent()) {
            return false;
        }

        return (course.get().getEnrolledUsers().contains(currentUser.get()));
    }

    public void deleteCourse(Long courseId, Authentication authentication) {
        Optional<AppUser> currentUser = appUserRepo.findByUsername(authentication.getPrincipal().toString());
        Optional<Course> course = courseRepo.findById(courseId);

        if (!currentUser.isPresent() || !course.isPresent()) {
            return;
        }

        if (course.get().getTutor() != currentUser.get()) {
            return;
        }

        courseRepo.delete(course.get());
    }

    public void leaveCourse(Long courseId, Authentication authentication) {
        Optional<AppUser> currentUser = appUserRepo.findByUsername(authentication.getPrincipal().toString());
        Optional<Course> course = courseRepo.findById(courseId);

        if (!currentUser.isPresent() || !course.isPresent()) {
            return;
        }

        if (!course.get().getEnrolledUsers().contains(currentUser.get())) {
            return;
        }

        if (course.get().getTutor() == currentUser.get()) {
            log.info("You are the tutor, you cant just leave like that!");
            return;
        }

        course.get().getEnrolledUsers().remove(currentUser.get());
    }

    public boolean canAnswer(Long taskId, Authentication authentication) {
        Optional<AppUser> currentUser = appUserRepo.findByUsername(authentication.getPrincipal().toString());
        Optional<Task> task = taskRepo.findById(taskId);

        if (!currentUser.isPresent() || !task.isPresent()) {
            return false;
        }

        if (currentUser.get() == task.get().getCourse().getTutor()) {
            return false;
        }

        if (!task.get().getCourse().getEnrolledUsers().contains(currentUser.get())) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(task.get().getEndsAt())) {
            return true;
        }

        return false;
    }

    public Course getCourseByTask(Long taskId, Authentication authentication) {
        Optional<Task> task = taskRepo.findById(taskId);
        List<Course> courses = courseRepo.findByTasks(task.get());
        return courses.get(0);
    }



}
