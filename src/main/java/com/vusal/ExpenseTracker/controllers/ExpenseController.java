package com.vusal.ExpenseTracker.controllers;

import com.vusal.ExpenseTracker.Entity.Category;
import com.vusal.ExpenseTracker.Entity.Expense;
import com.vusal.ExpenseTracker.Entity.User;
import com.vusal.ExpenseTracker.dto.ExpenseRequestDto;
import com.vusal.ExpenseTracker.dto.ExpenseResponseDto;
import com.vusal.ExpenseTracker.dto.ExpenseUpdateDto;
import com.vusal.ExpenseTracker.service.ExpenseService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/expense")
public class ExpenseController {
    private final ExpenseService expenseService;


    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;

    }
    @GetMapping("/{id}")
    public ExpenseResponseDto getExpense(@PathVariable Long id){
       Expense expense = expenseService.getExpense(id);
       Category category = expense.getCategory();
       String categoryName = category.getName();

        return new ExpenseResponseDto(
                expense.getAmount(),
                categoryName,
                expense.getDescription(),
                expense.getDate(),
                expense.getId());
    }
    @PostMapping
    public void createExpense(@RequestBody ExpenseRequestDto expense){
       expenseService.createExpense(expense);
    }
    @PutMapping("/{id}")
    public void updateExpense(@RequestBody ExpenseUpdateDto expense, @PathVariable Long id){
        expenseService.updateExpense(expense, id);
    }
    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable Long id){
        expenseService.deleteExpense(id);
    }
    @GetMapping("/user/{userId}/expenses")
    public List<ExpenseResponseDto> getExpenses(
            @PathVariable Long userId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) LocalDate date) {

        User user = expenseService.findUserById(userId);
        Category category= expenseService.findCategoryById(categoryId);
        List<Expense> expenses ;
        List<ExpenseResponseDto> responseDtoList = new ArrayList<>();

        if ( categoryId !=null && date !=null) {
            expenses= expenseService.findByUserAndCategoryAndTime(user,category,date);

        }
        else if(categoryId !=null){
            expenses = expenseService.findByUserAndCategory(user,category);

        }
        else if (date!=null){
            expenses=expenseService.findByUserAndDate(user,date);
        }
        else {
            expenses=expenseService.findByUser(user);
        }
        for(Expense e: expenses){
            ExpenseResponseDto responseDto= new ExpenseResponseDto();
            responseDto.setId(e.getId());
            responseDto.setDate(e.getDate());
            responseDto.setAmount(e.getAmount());
            responseDto.setDescription(e.getDescription());
            responseDto.setCategoryName(e.getCategory().getName());
            responseDtoList.add(responseDto);

        }
        return responseDtoList;

    }
}




