package com.vusal.ExpenseTracker.dto;

import com.vusal.ExpenseTracker.Entity.Expense;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ExpenseResponseDto {
   private Long id;
   private BigDecimal amount;
   private   String categoryName;
   private LocalDate date;
   private String description;
    public ExpenseResponseDto(BigDecimal amount, String categoryName, String description, LocalDate date,Long id){
        this.amount=amount;
        this.date=date;
        this.description=description;
        this.categoryName=categoryName;
        this.id=id;
    }

}
