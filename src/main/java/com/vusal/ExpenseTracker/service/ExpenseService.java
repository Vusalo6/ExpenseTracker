package com.vusal.ExpenseTracker.service;

import com.vusal.ExpenseTracker.Entity.Category;
import com.vusal.ExpenseTracker.Entity.Expense;
import com.vusal.ExpenseTracker.Entity.User;
import com.vusal.ExpenseTracker.repos.ExpenseRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.List;

@Service
public class ExpenseService { //Dependency injection in Constructions
    private final ExpenseRepo expenseRepo;
    public ExpenseService(ExpenseRepo expenseRepo){
        this.expenseRepo=expenseRepo;
    }
    public Expense getExpense(Long id){
       return expenseRepo.findById(id).orElseThrow();

    }
    public void creatExpense(Expense expense){
        expenseRepo.save(expense);
    }
    public void updateExpense(Expense expense,Long id){
        Expense existingExpense = expenseRepo.findById(id).orElseThrow();
        existingExpense.setUser(expense.getUser());
        existingExpense.setCategory(expense.getCategory());
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
        return  expenseRepo.findByUserAndCategory(user,category);
    }
    public List<Expense> findByUserAndDate(User user, LocalDate date){
      return expenseRepo.findByUserAndDate(user, date);
    }
}
