package com.vusal.ExpenseTracker.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class ExpenseResponseDto {
    Long id;
    BigDecimal amount;
    String categoryName;
    LocalDate date;
    String description;
    public ExpenseResponseDto(BigDecimal amount, String categoryName, String description, LocalDate date,Long id){
        this.amount=amount;
        this.date=date;
        this.description=description;
        this.categoryName=categoryName;
        this.id=id;
    }

}
