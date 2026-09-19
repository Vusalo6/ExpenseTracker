package com.vusal.ExpenseTracker.service;

import com.vusal.ExpenseTracker.Entity.Category;
import com.vusal.ExpenseTracker.Entity.Expense;
import com.vusal.ExpenseTracker.Entity.User;
import com.vusal.ExpenseTracker.dto.ExpenseRequestDto;
import com.vusal.ExpenseTracker.dto.ExpenseUpdateDto;
import com.vusal.ExpenseTracker.repos.CategoryRepo;
import com.vusal.ExpenseTracker.repos.ExpenseRepo;
import com.vusal.ExpenseTracker.repos.UserRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.List;

@Service
public class ExpenseService { //Dependency injection in Constructions
    private final ExpenseRepo expenseRepo;
    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;

    public ExpenseService(ExpenseRepo expenseRepo,UserRepo userRepo,CategoryRepo categoryRepo){
        this.expenseRepo=expenseRepo;
        this.userRepo=userRepo;
        this.categoryRepo=categoryRepo;
    }
    public Expense getExpense(Long id){
       return expenseRepo.findById(id).orElseThrow();

    }
    public void createExpense(ExpenseRequestDto expense){
        Expense newExpense= new Expense();
        newExpense.setUser(
                findUserById(expense.getUserId())
        );

        newExpense.setCategory(
                findCategoryById(expense.getCategoryId())
        );

        newExpense.setDescription(expense.getDescription());
        newExpense.setDate(expense.getDate());
        newExpense.setAmount(expense.getAmount());

        expenseRepo.save(newExpense);
    }
    public void updateExpense(ExpenseUpdateDto expense, Long id){
        Expense existingExpense = expenseRepo.findById(id).orElseThrow();
        Category category = findCategoryById(expense.getCategoryId());
        existingExpense.setCategory(category);
        existingExpense.setAmount(expense.getAmount());
        existingExpense.setDescription(expense.getDescription());
        existingExpense.setDate(expense.getDate());
        expenseRepo.save(existingExpense);
    }
    public void deleteExpense(Long id){
        expenseRepo.deleteById(id);
    }
    public List<Expense> findByUser(User user){
        return expenseRepo.findByUser(user);
    }
    public List<Expense> findByUserAndCategory(User user, Category category){
        return expenseRepo.findByUserAndCategory(user,category);

    }
    public List<Expense> findByUserAndDate(User user, LocalDate date){
      return expenseRepo.findByUserAndDate(user, date);
    }
    public List<Expense> findByUserAndCategoryAndTime(User user,Category category,LocalDate time){
        return expenseRepo.findByUserAndCategoryAndDate(user,category,time);
    }
    public Category findCategoryById(Long id){
        if (id == null) {
            return null;
        }
       return categoryRepo.findById(id).orElseThrow();
    }
    public User findUserById(Long id){
        return  userRepo.findById(id).orElseThrow();
    }
}
