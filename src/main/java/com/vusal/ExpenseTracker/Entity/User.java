package com.vusal.ExpenseTracker.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @GeneratedValue
    @Id
    Long id;
    String name;
    String surname;
    String email;
    @OneToMany(mappedBy = "user")
    List<Expense> expenses;



}
