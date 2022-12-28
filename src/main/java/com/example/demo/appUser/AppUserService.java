package com.example.demo.appUser;

import com.example.demo.appUserRole.AppUserRole;
import com.example.demo.appUserRole.AppUserRoleRepo;
import com.example.demo.course.Course;
import com.example.demo.course.CourseRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class AppUserService implements UserDetailsService {

    private final AppUserRepo appUserRepo;
    private final AppUserRoleRepo appUserRoleRepo;
    private final static String USER_NOT_FOUND_MESSAGE = "user with username %s not found";
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final CourseRepo courseRepo;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        AppUser appUser = appUserRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username)));

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        appUser.getAppUserRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return new org.springframework.security.core.userdetails.User(appUser.getUsername(), appUser.getPassword(), authorities);
    }

    public List<AppUser> getUsers() {
        return appUserRepo.findAll();
    }

    public List<AppUser> getUser(String username) {
        Optional<AppUser> optionalUser = appUserRepo.findByUsername(username);
        if (!optionalUser.isPresent()) {
            return null;
        }
        List<AppUser> users = new ArrayList<>();
        users.add(optionalUser.get());
        return users;
    }

    public AppUser saveUser(AppUser user) {
        if (user.getUsername().length() < 6) {
            throw new IllegalStateException("USERNAME_LENGTH");
        }

        if (user.getPassword().length() < 8) {
            throw new IllegalStateException("PASSWORD_LENGTH");
        }

        if (appUserRepo.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("USERNAME_TAKEN");
        }
        log.info("User saved: ", user.getUsername());

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return appUserRepo.save(user);
    }

    public AppUserRole saveRole(AppUserRole role) {
        return appUserRoleRepo.save(role);
    }

    public void addRoleToUser(String username, String roleName) {
        AppUser user = appUserRepo.findByUsername(username).get();
        AppUserRole role = appUserRoleRepo.findByName(roleName).get();
        user.getAppUserRoles().add(role);
    }

//    public boolean test(Long classroomId, Authentication authentication) {
//        Optional<AppUser> currentUser = appUserRepo.findByUsername(authentication.getPrincipal().toString());
//        Optional<Course> course = courseRepo.findById(classroomId);
//
//        if (!currentUser.isPresent() || !course.isPresent()) {
//            return false;
//        }
//
//        return course.get().isUserEnrolled(currentUser.get());
////        log.info(currentUser.get().getId().toString());
//    }

    public List<Course> getCourses(Authentication authentication) {
        Optional<AppUser> currentUser = appUserRepo.findByUsername(authentication.getPrincipal().toString());
        if (!currentUser.isPresent()) {
            return null;
        }
        return currentUser.get().getCourses();
    }
}
