package com.vusal.ExpenseTracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ExpenseRequestDto {

    private Long userId;
    private Long categoryId;
    private BigDecimal amount;
    private LocalDate date;
    private String description;


}
