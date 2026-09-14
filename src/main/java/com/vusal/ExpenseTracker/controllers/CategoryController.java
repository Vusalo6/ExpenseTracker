package com.vusal.ExpenseTracker.controllers;

import com.vusal.ExpenseTracker.Entity.Category;
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
    public Category getCategory(@PathVariable Long id){
       return categoryService.getCategory(id);
    }
    @PostMapping
    public void createCategory(@RequestBody Category category){
        categoryService.createCategory(category);
    }
    @PutMapping("/{id}")
    public void updateCategory(@RequestBody Category category, @PathVariable Long id) {
        categoryService.updateCategory(category, id);
    }
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
    }

}
