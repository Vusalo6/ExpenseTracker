package com.vusal.ExpenseTracker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private Long id;
    private   String name;
    private String surname;
    private String email;


    public UserResponseDto(Long id, String name, String surname, String email){
        this.id=id;
        this.name =name;
        this.surname=surname;
        this.email=email;
    }


}
