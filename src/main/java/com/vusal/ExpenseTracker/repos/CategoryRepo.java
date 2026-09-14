package com.vusal.ExpenseTracker.repos;

import com.vusal.ExpenseTracker.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category,Long> {
}
