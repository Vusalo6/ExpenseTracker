package com.vusal.ExpenseTracker.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;




@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "expenses")

public class Expense {
    @GeneratedValue
    @Id
    Long id;

    BigDecimal amount;
    LocalDate date;
    String description;
    @ManyToOne
    Category category;
    @ManyToOne
    User user ;


}
