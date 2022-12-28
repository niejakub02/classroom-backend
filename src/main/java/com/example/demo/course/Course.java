package com.example.demo.course;

import com.example.demo.answer.Answer;
import com.example.demo.appUser.AppUser;
import com.example.demo.task.Task;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true, nullable = false, length = 20)
    private String title;

    @Column(length = 50)
    private String about;

    @CreationTimestamp
    private LocalDateTime creationDateTime;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_enrolled",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<AppUser> enrolledUsers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    private AppUser tutor;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_pending",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<AppUser> pendingUsers = new ArrayList<>();

    public Course(String title, String about, AppUser tutor) {
        this.title = title;
        this.about = about;
        this.tutor = tutor;
        this.enrolledUsers.add(tutor);
    }

    public boolean isUserEnrolledOrPending(AppUser user) {
        return (getEnrolledUsers().contains(user) ||
                getPendingUsers().contains(user));
    }

    public boolean isUserEnrolledAndNotTutor(AppUser user) {
        return (getEnrolledUsers().contains(user) &&
                getTutor() != user);
    }
}
