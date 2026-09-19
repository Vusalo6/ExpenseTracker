package com.vusal.ExpenseTracker.service;

import com.vusal.ExpenseTracker.Entity.User;
import com.vusal.ExpenseTracker.dto.UserRequestDto;
import com.vusal.ExpenseTracker.repos.UserRepo;
import org.springframework.stereotype.Service;


@Service
public class UserService {                                           //this , userRepo extension
   private final UserRepo userRepo;
    public  UserService(UserRepo userRepo){
        this.userRepo=userRepo;
    }

    public User getUser(Long id){
        return userRepo.findById(id).orElseThrow();
    }
    public void saveUser(User user){
        userRepo.save(user);
    }
    public void deleteUser(Long id){
        userRepo.deleteById(id);
    }



    public void updateUser(UserRequestDto user, Long id){
       User existingUser = userRepo.findById(id).orElseThrow();
       existingUser.setName(user.getName());
       existingUser.setEmail(user.getEmail());
       existingUser.setSurname(user.getSurname());
        userRepo.save(existingUser);

    }

}
