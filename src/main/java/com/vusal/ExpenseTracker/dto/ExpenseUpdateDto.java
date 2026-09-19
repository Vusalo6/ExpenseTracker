package com.vusal.ExpenseTracker.dto;

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
    private Long categoryId;
    private BigDecimal amount;
    private LocalDate date;
    private String description;

}
