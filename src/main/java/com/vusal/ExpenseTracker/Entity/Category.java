package com.vusal.ExpenseTracker.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    @GeneratedValue
    @Id
    Long id;
    String name;
    @OneToMany(mappedBy = "category")
    List<Expense> expenses;

}
