package com.vusal.ExpenseTracker.controllers;

import com.vusal.ExpenseTracker.Entity.Category;
import com.vusal.ExpenseTracker.Entity.Expense;
import com.vusal.ExpenseTracker.Entity.User;
import com.vusal.ExpenseTracker.dto.ExpenseRequestDto;
import com.vusal.ExpenseTracker.dto.ExpenseResponseDto;
import com.vusal.ExpenseTracker.dto.ExpenseUpdateDto;
import com.vusal.ExpenseTracker.repos.ExpenseRepo;
import com.vusal.ExpenseTracker.repos.UserRepo;
import com.vusal.ExpenseTracker.service.CategoryService;
import com.vusal.ExpenseTracker.service.ExpenseService;
import com.vusal.ExpenseTracker.service.UserService;
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


        User user = expenseService.findUserById(userId);//the user for finding expense
        List <ExpenseResponseDto> responseDtoList  = new ArrayList<>(); //the list ResponseDto's will be saved and returned
//        ExpenseResponseDto responseDto =new ExpenseResponseDto(); //the ResponseDto object

        if (categoryId!=null && date!=null){
            Category category=expenseService.findCategoryById(categoryId);
            List<Expense> tempEx= expenseService.findByUserAndCategoryAndTime(user,category,date);
            for (Expense e:tempEx){
                ExpenseResponseDto responseDto =new ExpenseResponseDto();
                responseDto.setId(e.getId());
                responseDto.setDescription(e.getDescription());
                responseDto.setDate(e.getDate());
                responseDto.setAmount(e.getAmount());
                responseDto.setCategoryName(e.getCategory().getName());
                responseDtoList.add(responseDto);
            }
        }



        else if (categoryId != null ) {

            Category category = expenseService.findCategoryById(categoryId);
            List<Expense> expenses = expenseService.findByUserAndCategory(user,category);
            for (Expense e :expenses){
                ExpenseResponseDto responseDto =new ExpenseResponseDto();
                responseDto.setId(e.getId());
                responseDto.setDescription(e.getDescription());
                responseDto.setDate(e.getDate());
                responseDto.setAmount(e.getAmount());
                responseDto.setCategoryName(e.getCategory().getName());
                responseDtoList.add(responseDto);
            }

        }

         else if (date != null ) {
            List<Expense> expenses = expenseService.findByUserAndDate(user, date);
             for(Expense e: expenses){
                 ExpenseResponseDto responseDto =new ExpenseResponseDto();
                 responseDto.setId(e.getId());
                 responseDto.setDescription(e.getDescription());
                 responseDto.setDate(e.getDate());
                 responseDto.setAmount(e.getAmount());
                 responseDto.setCategoryName(e.getCategory().getName());
                 responseDtoList.add(responseDto);
             }
        }
        else {
            List<Expense> expenses = expenseService.findByUser(user);
            for (Expense e :expenses){
                ExpenseResponseDto responseDto =new ExpenseResponseDto();
                responseDto.setId(e.getId());
                responseDto.setDescription(e.getDescription());
                responseDto.setDate(e.getDate());
                responseDto.setAmount(e.getAmount());
                responseDto.setCategoryName(e.getCategory().getName());
                responseDtoList.add(responseDto);
            }
        }
        return responseDtoList;
        }
    }




