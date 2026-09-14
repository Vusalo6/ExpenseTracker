package com.vusal.ExpenseTracker.dto;

import com.vusal.ExpenseTracker.Entity.User;

public class UserDto {
    User user;
    String name;
    String surname;
    String email;

    public UserDto(User user){
        this.user = user;
        this.name =user.getName();
        this.surname=user.getSurname();
        this.email=user.getEmail();
    }


}
