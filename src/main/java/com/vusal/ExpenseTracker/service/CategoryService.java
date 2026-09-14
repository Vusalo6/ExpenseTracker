package com.vusal.ExpenseTracker.service;

import com.vusal.ExpenseTracker.Entity.Category;
import com.vusal.ExpenseTracker.repos.CategoryRepo;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepo categoryRepo;
    public CategoryService(CategoryRepo categoryRepo){
        this.categoryRepo=categoryRepo;
    }
    public Category getCategory(Long id){
       return categoryRepo.findById(id).orElseThrow();
    }
    public void createCategory(Category category){
        categoryRepo.save(category);
    }
    public void deleteCategory(Long iD){
        categoryRepo.deleteById(iD);
    }
    public void updateCategory(Category category,Long id){
        Category existingCategory = categoryRepo.findById(id).orElseThrow();
        existingCategory.setName(category.getName());
        categoryRepo.save(existingCategory);
    }


}
