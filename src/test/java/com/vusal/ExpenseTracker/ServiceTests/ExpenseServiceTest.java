package com.vusal.ExpenseTracker.ServiceTests;

import com.vusal.ExpenseTracker.Entity.Category;
import com.vusal.ExpenseTracker.Entity.Expense;
import com.vusal.ExpenseTracker.Entity.User;
import com.vusal.ExpenseTracker.dto.ExpenseRequestDto;
import com.vusal.ExpenseTracker.dto.ExpenseUpdateDto;
import com.vusal.ExpenseTracker.exception.ResourceNotFoundException;
import com.vusal.ExpenseTracker.repos.CategoryRepo;
import com.vusal.ExpenseTracker.repos.ExpenseRepo;
import com.vusal.ExpenseTracker.repos.UserRepo;
import com.vusal.ExpenseTracker.service.ExpenseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    ExpenseRepo expenseRepo;
    @Mock
    UserRepo userRepo;
    @Mock
    CategoryRepo categoryRepo;
    @InjectMocks
    ExpenseService expenseService;



    @Test
    void getTotalTest(){
        Expense expense = new Expense();
        expense.setAmount(BigDecimal.valueOf(300));
        Expense expense1 = new Expense();
        expense1.setAmount(BigDecimal.valueOf(400));

        List<Expense> expenses= new ArrayList<>();
        expenses.add(expense);
        expenses.add(expense1);
        BigDecimal total = expenseService.getTotal(expenses);
               assertEquals(BigDecimal.valueOf(700),total);

    }
    @Test
  void getTotal_whenListIsEmpty_returnsZero(){
        List<Expense> expenses = new ArrayList<>();
        BigDecimal total = expenseService.getTotal(expenses);
        assertEquals(BigDecimal.ZERO,total);
    }

        @Test
        void expenseCreationTest() {

            // ARRANGE

            String email = "test@gmail.com";

            User user = new User();
            user.setEmail(email);

            Category category = new Category();

            ExpenseRequestDto dto = new ExpenseRequestDto();
            dto.setAmount(BigDecimal.valueOf(100));
            dto.setDescription("AA");
            dto.setCategoryId(1L);
            dto.setDate(LocalDate.now());

            // Tell Mockito what the repositories should return
            when(userRepo.findUserByEmail(email)).thenReturn(Optional.of(user));

            when(categoryRepo.findById(1L)).thenReturn(Optional.of(category));
            ArgumentCaptor<Expense> captor =
                    ArgumentCaptor.forClass(Expense.class);


            // ACT

            expenseService.createExpense(dto, email);

            // ASSERT

            verify(expenseRepo).save(captor.capture());
            Expense capturedExpenseDto = captor.getValue();
            assertEquals(dto.getAmount(),capturedExpenseDto.getAmount());
            assertEquals(dto.getDate(),capturedExpenseDto.getDate());
            assertEquals(category,capturedExpenseDto.getCategory());

        }

        @Test
        void expenseCreationWhenCategoryDoNotExist()   {
           //Arrange
            User user = new User();
            String email= "test@gmail.com";
            user.setEmail(email);
            ExpenseRequestDto requestDto = new ExpenseRequestDto();
            requestDto.setDate(LocalDate.now());
            requestDto.setDescription("AA");
            requestDto.setAmount(BigDecimal.valueOf(100));
            requestDto.setCategoryId(3L);
            //Act
            when(userRepo.findUserByEmail(email)).thenReturn(Optional.of(user));
            when(categoryRepo.findById(3L)).thenReturn(Optional.empty());
            //Assert
            Exception exception = assertThrows(ResourceNotFoundException.class,()->expenseService.createExpense(requestDto,email));
            String expectedMessage ="Category Doesnt exist";
            String actualMessage = exception.getMessage();
            assertEquals(expectedMessage,actualMessage);
            verify(expenseRepo, never()).save(any(Expense.class));
        }

        @Test
    void expenseCreationWhenUserDoNotExist(){
            //Arrange
            ExpenseRequestDto dto =new ExpenseRequestDto();
            String email = "test@gmail.com";

            //Act
            when(userRepo.findUserByEmail(email))
                    .thenReturn(Optional.empty());
            //Assert
           ResourceNotFoundException exception= assertThrows
                   (ResourceNotFoundException.class,
                           ()->expenseService.createExpense(dto,email));

            String actualMessage =
                    exception.getMessage();

            assertEquals
                    ( "The user with this email doesnt exist",actualMessage);

            //Verifying
            verify(expenseRepo, never()).save(any(Expense.class));
            verifyNoInteractions(categoryRepo);
        }

        @Test
        void updateExpenseWhenExpenseDoesntBelongToUser(){
            //Arrange
            String email = "test@email.com";
            ExpenseUpdateDto updateDto = new ExpenseUpdateDto();

            when(expenseRepo
                    .findByIdAndUser_Email(1L,email))
                    .thenReturn(Optional.empty());
            //Assert & Act
          ResourceNotFoundException exception =assertThrows(ResourceNotFoundException.class,
                  ()->expenseService.updateExpense(updateDto,1L,email));

          assertEquals("Expense doesnt exist for update",exception.getMessage());

          //Verify
          verify(expenseRepo,
                  never())
                  .save(any(Expense.class));

        }

        @Test
        void ExpenseUpdateSuccessCheck(){
        //Arrange
            Expense expense =new Expense();
            Category category = new Category();
            category.setId(1L);

            ExpenseUpdateDto updateDto = new ExpenseUpdateDto();
                             updateDto.setCategoryId(1L);
                             updateDto.setAmount(BigDecimal.valueOf(100));
                             updateDto.setDescription("AA");
                             updateDto.setDate(LocalDate.now());

            String email ="test@gmail.com";
            when(expenseRepo
                    .findByIdAndUser_Email(1L,email))
                    .thenReturn((Optional.of(expense)));
            when(categoryRepo
                    .findById(1L))
                    .thenReturn(Optional.of(category));
            // ArgumentCaptor
            ArgumentCaptor<Expense> captor = ArgumentCaptor.forClass(Expense.class);

            //Act
            expenseService.updateExpense(updateDto,1L,email);
            //Verify
            verify(expenseRepo).save(captor.capture());
            Expense capturedExpenseDto = captor.getValue();

            //Assertion
            LocalDate expectedDate = updateDto.getDate();
            LocalDate actualDate = capturedExpenseDto.getDate();

            BigDecimal expectedAmount =updateDto.getAmount();
            BigDecimal actualAmount = capturedExpenseDto.getAmount();

            String expectedDescription=updateDto.getDescription();
            String  actualDescription = capturedExpenseDto.getDescription();

            assertEquals(capturedExpenseDto.getCategory().getId(), updateDto.getCategoryId());
            assertEquals(expectedDate,actualDate);
            assertEquals(expectedAmount,actualAmount);
            assertEquals(expectedDescription,actualDescription);
        }
        @Test
         void successfulDelete(){

            //Arrange
            String email ="test@emai.com";
            Expense expense = new Expense();

            when(expenseRepo.findByIdAndUser_Email(1L,email)).thenReturn(Optional.of(expense));
            //Act
            expenseService.deleteExpense(1L,email);
            //Assert & Verify
            verify(expenseRepo).delete(expense);

        }
        @Test
        void expenseDoNotExistToDelete(){
            //Arrange
            String email = "test@gmail.com";
          when(expenseRepo.findByIdAndUser_Email(1L,email)).thenReturn(Optional.empty());


            //Assert & Act
          Exception exception = assertThrows(ResourceNotFoundException.class,()-> expenseService.deleteExpense(1L,email));
           //Verify & Assert
            String expected ="Resource not found";
            String actual =exception.getMessage();
            assertEquals(expected,actual);
            verify(expenseRepo, never()).delete(any(Expense.class));
    }





    }
