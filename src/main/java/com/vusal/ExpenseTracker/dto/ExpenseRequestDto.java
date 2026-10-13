package com.vusal.ExpenseTracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ExpenseRequestDto {

    private Long userId;
    private Long categoryId;
    @NotNull(message = "amount is required")
    @Positive(message = "amount must be greater than 0")
    private BigDecimal amount;
    @NotNull(message = "Date is required")
    private LocalDate date;
    @NotBlank(message = "Description is required")
    private String description;


}
