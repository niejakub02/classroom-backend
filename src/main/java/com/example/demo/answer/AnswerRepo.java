package com.example.demo.answer;

import com.example.demo.appUser.AppUser;
import com.example.demo.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AnswerRepo extends JpaRepository<Answer, Long> {
    Optional<Answer> findByTaskAndAnsweredBy(Task task, AppUser user);
}
