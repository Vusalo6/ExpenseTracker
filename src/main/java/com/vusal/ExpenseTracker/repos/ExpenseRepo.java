package com.vusal.ExpenseTracker.repos;

import com.vusal.ExpenseTracker.Entity.Category;
import com.vusal.ExpenseTracker.Entity.Expense;
import com.vusal.ExpenseTracker.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepo extends JpaRepository<Expense,Long> {
    List<Expense> findByUser(User user);
    List<Expense> findByUserAndCategory(User user, Category category);
    List<Expense> findByUserAndDate(User user, LocalDate time);
    List<Expense> findByUserAndCategoryAndDate(User user,Category category,LocalDate time);
    List<Expense> findByUserAndDateBetween(User user, LocalDate fromDate,LocalDate toDate);
    Optional<Expense> findByIdAndUser_Email(Long id, String email);

}
