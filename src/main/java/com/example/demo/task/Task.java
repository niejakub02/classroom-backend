package com.example.demo.task;

import com.example.demo.answer.Answer;
import com.example.demo.course.Course;
import com.example.demo.forms.TaskForm;
import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime startsAt;

    private LocalDateTime endsAt;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnore
    private Course course;

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Answer> answers = new ArrayList<>();

    public Task(TaskForm taskForm, Course course) {
        this.title = taskForm.getTitle();
        this.content = taskForm.getContent();
        this.startsAt = taskForm.getStartsAt();
        this.endsAt = taskForm.getEndsAt();
        this.course = course;
    }
}
