package com.example.demo.appUserRole;

import com.example.demo.appUser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AppUserRoleRepo extends JpaRepository<AppUserRole, Long> {
    Optional<AppUserRole> findByName(String name);
}
