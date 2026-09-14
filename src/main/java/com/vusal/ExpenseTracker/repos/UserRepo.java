package com.vusal.ExpenseTracker.repos;

import com.vusal.ExpenseTracker.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {
}
