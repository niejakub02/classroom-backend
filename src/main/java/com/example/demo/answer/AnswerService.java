package com.example.demo.answer;

import com.example.demo.appUser.AppUser;
import com.example.demo.appUser.AppUserRepo;
import com.example.demo.forms.AddScoreForm;
import com.example.demo.forms.EditAnswerForm;
import com.example.demo.task.Task;
import com.example.demo.task.TaskRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class AnswerService {
    private final AnswerRepo answerRepo;
    private final TaskRepo taskRepo;
    private final AppUserRepo appUserRepo;

    public List<Answer> getAnswer(Long taskId, Authentication authentication) {
        Optional<Task> task = taskRepo.findById(taskId);
        Optional<AppUser> currentUser = appUserRepo.findByUsername(authentication.getPrincipal().toString());

        Optional<Answer> answer = answerRepo.findByTaskAndAnsweredBy(task.get(), currentUser.get());
        List<Answer> answers = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(task.get().getEndsAt())) {
            return answers;
            // should be an error
        }

        if (answer.isPresent()) {
            answers.add(answer.get());
        } else {
            Answer newAnswer = answerRepo.save(new Answer(task.get(), currentUser.get()));
            answers.add(newAnswer);
        }


        return answers;
    }

    public List<Answer> getAnswers() {
        return answerRepo.findAll();
    }

    public void editAnswer(EditAnswerForm editAnswerForm, Long answerId, Authentication authentication) {
        Optional<AppUser> currentUser = appUserRepo.findByUsername(authentication.getPrincipal().toString());
        Optional<Answer> answer = answerRepo.findById(answerId);

        if (!currentUser.isPresent() || !answer.isPresent()) {
            return;
        }

        if (answer.get().getAnsweredBy() != currentUser.get()) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(answer.get().getTask().getEndsAt())) {
            return;
        }

        answer.get().setContent(editAnswerForm.getContent());
        return;
    }

    public void deleteAnswer(Long answerId, Authentication authentication) {
        Optional<AppUser> currentUser = appUserRepo.findByUsername(authentication.getPrincipal().toString());
        Optional<Answer> answer = answerRepo.findById(answerId);

        if (!currentUser.isPresent() || !answer.isPresent()) {
            return;
        }

        if (answer.get().getAnsweredBy() != currentUser.get()) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(answer.get().getTask().getEndsAt())) {
            return;
        }

        answerRepo.delete(answer.get());
        return;
    }

    public void addScoreToAnswer(AddScoreForm addScoreForm, Long answerId, Authentication authentication) {
        Optional<AppUser> currentUser = appUserRepo.findByUsername(authentication.getPrincipal().toString());
        Optional<Answer> answer = answerRepo.findById(answerId);

        if (!currentUser.isPresent() || !answer.isPresent()) {
            return;
        }

        if (addScoreForm.getScore() > 10 || addScoreForm.getScore() < 0) {
            return;
        }

        Task task = answer.get().getTask();

        if (currentUser.get() != task.getCourse().getTutor()) {
            return;
        }

        answer.get().setScore(addScoreForm.getScore());
        return;
    }
}
