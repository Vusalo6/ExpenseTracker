package com.vusal.ExpenseTracker.controllers;

import com.vusal.ExpenseTracker.Entity.Category;
import com.vusal.ExpenseTracker.dto.CategoryRequestDto;
import com.vusal.ExpenseTracker.dto.CategoryResponseDto;
import com.vusal.ExpenseTracker.service.CategoryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {
   private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping("/{id}")
    public CategoryResponseDto getCategory(@PathVariable Long id){
       Category category = categoryService.getCategory(id);
       return new CategoryResponseDto(
               category.getId(),
               category.getName());
    }
    @PostMapping
    public void createCategory(@RequestBody CategoryRequestDto category){
        Category newCategory = new Category(); //is it correct ? bcs we dont set List expenses
        newCategory.setName(category.getName());
        categoryService.saveCategory(newCategory);
    }
    @PutMapping("/{id}")
    public void updateCategory(@RequestBody CategoryRequestDto category, @PathVariable Long id) {
        categoryService.updateCategory(category, id);
    }
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
    }

}
