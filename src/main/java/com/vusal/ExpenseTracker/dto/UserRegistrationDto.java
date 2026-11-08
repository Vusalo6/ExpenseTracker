package com.vusal.ExpenseTracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserRegistrationDto {
    @NotBlank(message = "Name is required ")
    private String name;
    @NotBlank(message = "Surname is required")
    private String surname;
    @NotBlank(message = "Password is required")
    String password;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
}
