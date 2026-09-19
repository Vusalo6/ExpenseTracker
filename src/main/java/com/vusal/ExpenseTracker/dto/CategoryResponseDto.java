package com.vusal.ExpenseTracker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponseDto {
    Long id;
    String name;
    public CategoryResponseDto(Long id, String name){
        this.id=id;
        this.name=name;
    }
}
