package com.example.demo.appUser;

import com.example.demo.answer.Answer;
import com.example.demo.appUserRole.AppUserRole;
import com.example.demo.course.Course;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Slf4j
public class AppUser implements UserDetails {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<AppUserRole> AppUserRoles = new ArrayList<>();

    private boolean enabled = false;

    private boolean locked = false;

    @JsonIgnore
    @ManyToMany(mappedBy = "enrolledUsers", fetch = FetchType.LAZY)
    private List<Course> courses = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY)
    private List<Course> tutors = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "pendingUsers", fetch = FetchType.LAZY)
    private List<Course> pendingCourses = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "answeredBy", fetch = FetchType.LAZY)
    private List<Answer> answers = new ArrayList<>();

    public AppUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !enabled;
    }
}
