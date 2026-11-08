package com.vusal.ExpenseTracker.repos;

import com.vusal.ExpenseTracker.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findUserByEmail(String email);
}
