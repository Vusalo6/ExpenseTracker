package com.vusal.ExpenseTracker.controllers;

import com.vusal.ExpenseTracker.Entity.Category;
import com.vusal.ExpenseTracker.Entity.Expense;
import com.vusal.ExpenseTracker.Entity.User;
import com.vusal.ExpenseTracker.service.CategoryService;
import com.vusal.ExpenseTracker.service.ExpenseService;
import com.vusal.ExpenseTracker.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/expense")
public class ExpenseController {
    private final ExpenseService expenseService;
    private final UserService userService;
    private final CategoryService categoryService;

    public ExpenseController(ExpenseService expenseService,UserService userService,CategoryService categoryService) {
        this.expenseService = expenseService;
        this.userService=userService;
        this.categoryService=categoryService;
    }
    @GetMapping("/{id}")
    public Expense getExpense(@PathVariable Long id){
       return expenseService.getExpense(id);
    }
    @PostMapping
    public void createExpense(@RequestBody Expense expense){
        expenseService.creatExpense(expense);

    }
    @PutMapping("/{id}")
    public void updateExpense(@RequestBody Expense expense,@PathVariable Long id){
        expenseService.updateExpense(expense, id);
    }
    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable Long id){
        expenseService.deleteExpense(id);
    }
    @GetMapping("/user/{userId}/expenses")
    public List<Expense> getExpenses(
            @PathVariable Long userId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) LocalDate date) {

        User user = userService.getUser(userId);

        if (categoryId != null) {
            Category category = categoryService.getCategory(categoryId);
            return expenseService.findByUserAndCategory(user, category);
        }

        if (date != null) {
            return expenseService.findByUserAndDate(user, date);
        }

        return expenseService.findByUser(user);
    }
}



