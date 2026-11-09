package com.vusal.ExpenseTracker.controllers;

import com.vusal.ExpenseTracker.Entity.Category;
import com.vusal.ExpenseTracker.Entity.Expense;
import com.vusal.ExpenseTracker.Entity.User;
import com.vusal.ExpenseTracker.dto.ExpenseListResponseDto;
import com.vusal.ExpenseTracker.dto.ExpenseRequestDto;
import com.vusal.ExpenseTracker.dto.ExpenseUpdateDto;
import com.vusal.ExpenseTracker.service.ExpenseService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest( controllers = ExpenseController.class)
class ExpenseControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ExpenseService expenseService;


    @Test
    void createExpense_validRequest_returnsCreated() throws Exception {

        // ARRANGE

        String email = "test@gmail.com";

        ExpenseRequestDto dto = new ExpenseRequestDto();
        dto.setCategoryId(1L);
        dto.setAmount(BigDecimal.valueOf(100));
        dto.setDate(LocalDate.of(2026, 11, 8));
        dto.setDescription("AA");


        // ACT
        mockMvc.perform(
                        post("/expense")
                                .with(jwt().jwt(token -> token.subject(email)))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isCreated());


        // Verify

        ArgumentCaptor<ExpenseRequestDto> captor =
                ArgumentCaptor.forClass(ExpenseRequestDto.class);

        verify(expenseService).createExpense(captor.capture(), eq(email));

        ExpenseRequestDto capturedDto = captor.getValue();

        assertEquals(dto.getCategoryId(), capturedDto.getCategoryId());
        assertEquals(dto.getAmount(), capturedDto.getAmount());
        assertEquals(dto.getDate(), capturedDto.getDate());
        assertEquals(dto.getDescription(), capturedDto.getDescription());
    }

    @Test
    void createExpenseWhenAmountIsZero() throws Exception {
        //Arrange
        ExpenseRequestDto dto = new ExpenseRequestDto();
        String email = "test@gmail.com";
        dto.setAmount(BigDecimal.valueOf(0));
        dto.setDate(LocalDate.of(2026,11,11));
        dto.setDescription("AA");
        dto.setCategoryId(1L);

        //ACT
        mockMvc.perform(post("/expense")
                .with(jwt().jwt(token->token.subject(email)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
        //Verify
        verify(expenseService,never()).createExpense(any(ExpenseRequestDto.class),eq(email));

    }
    @Test
    void getExpense()throws Exception {
        //Arrange
        String email = "test@gmail.com";
        Long id =1L;
        Category category = new Category();
        User user =new User();
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setCategory(category);
        expense.setUser(user);
        expense.setAmount(BigDecimal.valueOf(100));
        expense.setDate(LocalDate.now());
        expense.setDescription("AA");

        when(expenseService.returnExpenseForUser(id,email)).thenReturn(expense);
        //Act & Assert
        mockMvc.perform(
                get("/expense/{id}",id)
                        .with(jwt().jwt(token->token.subject(email)))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("AA"))
                .andExpect(jsonPath("$.id").value(1L));


        //Verify
        verify(expenseService).returnExpenseForUser(id,email);
    }
    @Test
    void getAuthenticatedUserExpenses()throws Exception {
        //Arrange
        String email = "test@email.com";
        ExpenseListResponseDto dto = new ExpenseListResponseDto();
        dto.setTotal(BigDecimal.valueOf(100));

        when(expenseService.getExpensesForAuthenticatedUser(email))
                .thenReturn(dto);
        //Act
        mockMvc.perform(get("/expense/me/expenses")
                .with(jwt().jwt(token->token.subject(email)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(BigDecimal.valueOf(100)));

        //Asser & Verify
        verify(expenseService).getExpensesForAuthenticatedUser(email);
    }
    @Test
    void putWorksTest ()throws Exception{
        //Arrange
        String email = "test@gmail.com";
        Long id = 1L;
        ExpenseUpdateDto dto = new ExpenseUpdateDto();
        dto.setDescription("AA");
        dto.setAmount(BigDecimal.valueOf(100));
        dto.setDate(LocalDate.now());
        dto.setCategoryId(1L);


        //Act;
        mockMvc.perform(put("/expense/{id}",id)
                .with(jwt().jwt(token->token.subject(email)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))

        )
                .andExpect(status().isOk());




        //Verify & Assert
        ArgumentCaptor<ExpenseUpdateDto> captor = ArgumentCaptor.forClass(ExpenseUpdateDto.class);

        verify(expenseService).updateExpense(
                captor.capture(),
                eq(id),
                eq(email)
                );
        ExpenseUpdateDto capturedDto = captor.getValue();

        assertEquals(dto.getAmount(), capturedDto.getAmount());
        assertEquals(dto.getDate(), capturedDto.getDate());
        assertEquals(dto.getCategoryId(), capturedDto.getCategoryId());
        assertEquals(dto.getDescription(), capturedDto.getDescription());
    }
    @Test
    void putNotWorkingWhenAmountIsZero() throws  Exception{
        //Arrange
        ExpenseUpdateDto dto = new ExpenseUpdateDto();
        Long id = 1L;
        dto.setAmount(BigDecimal.valueOf(0));
        dto.setDescription("AA");
        dto.setDate(LocalDate.now());
        dto.setCategoryId(1L);
        String email ="test@gmail.com";

        //Act
        mockMvc.perform(put("/expense/{id}",id)
                        .with(jwt().jwt(token->token.subject(email)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest());
        //Assert & Verify
        verify(expenseService,never()).updateExpense(any(ExpenseUpdateDto.class),eq(id),eq(email));

    }
    @Test
    void successfullyDelete()throws Exception{
        //Arrange
        Long id = 1L;
        String email = "test@email.com";

        //Act
        mockMvc.perform(delete("/expense/{id}",id)
                        .with(jwt().jwt(token->token.subject(email))))
                .andExpect(status().isNoContent());
        //Assert
        verify(expenseService).deleteExpense(id,email);

    }
    @Test
    void getUsersAllExpensesBYDate()throws Exception {
        //Arrange
        Long id = 1L;
        User user =new User();
        Category category = new Category();
        List<Expense> expenses = new ArrayList<>();
        BigDecimal total = BigDecimal.valueOf(100);
        LocalDate date = LocalDate.now();

        when(expenseService.findUserById(id)).thenReturn(user);
        when(expenseService.findCategoryById(id)).thenReturn(category);
        when(expenseService.getTotal(expenses)).thenReturn(total);
        when(expenseService.findByUserAndCategoryAndDate(user,category,date)).thenReturn(expenses);



        //Act
        mockMvc.perform(get("/expense/user/{userId}/expenses",id)
                .param("date","2026/11/11")
                        .param("categoryId", String.valueOf(id))
                        .param("userId",String.valueOf(id))
        )
                .andExpect(status().isOk());
        //Assert

    }


}

