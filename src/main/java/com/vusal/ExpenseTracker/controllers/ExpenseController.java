package com.vusal.ExpenseTracker.controllers;

import com.vusal.ExpenseTracker.Entity.Category;
import com.vusal.ExpenseTracker.Entity.Expense;
import com.vusal.ExpenseTracker.Entity.User;
import com.vusal.ExpenseTracker.dto.ExpenseListResponseDto;
import com.vusal.ExpenseTracker.dto.ExpenseRequestDto;
import com.vusal.ExpenseTracker.dto.ExpenseResponseDto;
import com.vusal.ExpenseTracker.dto.ExpenseUpdateDto;
import com.vusal.ExpenseTracker.enums.Period;
import com.vusal.ExpenseTracker.repos.UserRepo;
import com.vusal.ExpenseTracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    public ExpenseResponseDto getExpense(@PathVariable Long id,@AuthenticationPrincipal Jwt jwt){
       Expense expense = expenseService.returnExpenseForUser(id,jwt.getSubject());

        return new ExpenseResponseDto(
                expense.getAmount(),
                expense.getCategory().getName(),
                expense.getDescription(),
                expense.getDate(),
                expense.getId());
    }

    @GetMapping("/me/expenses")
    public ExpenseListResponseDto getExpenses(@AuthenticationPrincipal Jwt jwt){
    return expenseService.getExpensesForAuthenticatedUser(jwt.getSubject());


    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createExpense(
            @RequestBody
            @Valid
            ExpenseRequestDto expenseRequestDto,
            @AuthenticationPrincipal Jwt jwt) {
       expenseService.createExpense(expenseRequestDto,jwt.getSubject());
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateExpense(@Valid @RequestBody ExpenseUpdateDto expense, @PathVariable Long id,@AuthenticationPrincipal Jwt jwt){
        expenseService.updateExpense(expense, id,jwt.getSubject());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpense(@PathVariable Long id,@AuthenticationPrincipal Jwt jwt){
        expenseService.deleteExpense(id,jwt.getSubject());
    }

    @GetMapping("/user/me/expenses")
    public ExpenseListResponseDto getExpenses(
            @PathVariable Long userId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false)Period period,
            @RequestParam(required = false) LocalDate date) {
        if (date != null && period != null) {
            throw new IllegalArgumentException("date and period cannot be used together");
        }

        User user = expenseService.findUserById(userId);
        Category category = expenseService.findCategoryById(categoryId);
        List<Expense> expenses;
        List<ExpenseResponseDto> responseDtoList = new ArrayList<>();

        if (categoryId != null && date != null) {

            expenses = expenseService.findByUserAndCategoryAndDate(user, category, date
            );

        } else if (categoryId != null) {

            expenses = expenseService.findByUserAndCategory(user, category);

        } else if (date != null) {

            expenses = expenseService.findByUserAndDate(user, date);

        } else if (period != null) {

            expenses = switch (period) {
                case THIS_WEEK -> expenseService.getThisWeekExpenses(user);
                case LAST_WEEK -> expenseService.getLastWeekExpenses(user);
                case THIS_MONTH -> expenseService.getThisMonthExpenses(user);
                case LAST_MONTH -> expenseService.getLastMonthExpenses(user);
            };

        } else {

            expenses = expenseService.findByUser(user);
        }

        BigDecimal total = expenseService.getTotal(expenses);

        for (Expense e : expenses) {
            ExpenseResponseDto responseDto = new ExpenseResponseDto();

            responseDto.setId(e.getId());
            responseDto.setDate(e.getDate());
            responseDto.setAmount(e.getAmount());
            responseDto.setDescription(e.getDescription());
            responseDto.setCategoryName(e.getCategory().getName());

            responseDtoList.add(responseDto);
        }

        return new ExpenseListResponseDto(responseDtoList, total);

        }
    }






