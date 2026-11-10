package com.vusal.ExpenseTracker.controllers;

import com.vusal.ExpenseTracker.Entity.Category;
import com.vusal.ExpenseTracker.Entity.Expense;
import com.vusal.ExpenseTracker.dto.ExpenseResponseDtoList;
import com.vusal.ExpenseTracker.dto.ExpenseRequestDto;
import com.vusal.ExpenseTracker.dto.ExpenseResponseDto;
import com.vusal.ExpenseTracker.dto.ExpenseUpdateDto;
import com.vusal.ExpenseTracker.enums.Period;
import com.vusal.ExpenseTracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public ExpenseResponseDtoList getExpenses(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false)Period period,
            @RequestParam(required = false) LocalDate date){
        ExpenseResponseDtoList responseDtoList= new ExpenseResponseDtoList();

       if (period!=null && date!=null){
           throw new IllegalArgumentException("Date and period cannot be used together");
       }
       else if (jwt!=null && date!=null &&categoryId!=null) {
           responseDtoList =  expenseService.findByUserAndCategoryAndDate(jwt.getSubject(),categoryId,date);
       }
       else if (jwt!=null && categoryId==null && period==null&& date==null ) {
          responseDtoList = expenseService.getExpensesForAuthenticatedUser(jwt.getSubject());
       }
       else if (jwt!=null && date!=null && categoryId==null){
           responseDtoList = expenseService.findByUserAndDate(jwt.getSubject(), date);
       }
        else if (jwt != null && categoryId!= null) {
         responseDtoList =  expenseService
                    .findByUserAndCategory
                            (jwt.getSubject(),categoryId);
        }
        else if (jwt!=null && categoryId==null && period!=null){
          responseDtoList=  expenseService.getByUserAndPeriod(jwt.getSubject(),period);
       }


        return responseDtoList;


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



    }






