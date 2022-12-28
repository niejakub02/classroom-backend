package com.example.demo.course;

import com.example.demo.appUser.AppUser;
import com.example.demo.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface CourseRepo extends JpaRepository<Course, Long> {
    List<Course> findByEnrolledUsers(AppUser user);
    List<Course> findByTutor(AppUser user);
    List<Course> findByPendingUsers(AppUser user);
    List<Course> findByTasks(Task task);
}
