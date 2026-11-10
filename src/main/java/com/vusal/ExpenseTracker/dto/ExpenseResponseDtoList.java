package com.vusal.ExpenseTracker.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@NoArgsConstructor
@Getter
    @Setter
    public class ExpenseResponseDtoList {

        private List<ExpenseResponseDto> expenses;
        private BigDecimal total;

        public ExpenseResponseDtoList(List<ExpenseResponseDto> expenses, BigDecimal total) {
            this.expenses = expenses;
            this.total = total;
        }
    }

