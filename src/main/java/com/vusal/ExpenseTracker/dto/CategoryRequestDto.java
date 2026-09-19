package com.vusal.ExpenseTracker.dto;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class CategoryRequestDto {
    String name;
    public CategoryRequestDto(String name){
        this.name=name;
    }
}
