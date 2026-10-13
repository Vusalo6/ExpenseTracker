package com.vusal.ExpenseTracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseUpdateDto {
    @NotBlank(message = "Category iD is required")
    private Long categoryId;
    @NotBlank(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private BigDecimal amount;
    @NotBlank(message = "Date is required")
    private LocalDate date;
    @NotBlank(message = "Description is required")
    @Size(max = 150, message = "Description cannot exceed 150 characters")
    private String description;

}
