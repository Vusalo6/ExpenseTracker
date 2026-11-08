package com.vusal.ExpenseTracker.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private Long id;
    private String name;
    private String surname;
    private String password;
    private String email;
    @OneToMany(mappedBy = "user")
    List<Expense> expenses;



}
