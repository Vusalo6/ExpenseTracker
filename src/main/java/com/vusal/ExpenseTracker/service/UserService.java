package com.vusal.ExpenseTracker.service;

import com.vusal.ExpenseTracker.Entity.User;
import com.vusal.ExpenseTracker.dto.UserRegistrationDto;
import com.vusal.ExpenseTracker.dto.UserRequestDto;
import com.vusal.ExpenseTracker.exception.ResourceNotFoundException;
import com.vusal.ExpenseTracker.repos.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {                                           //this , userRepo extension
   private final UserRepo userRepo;
   private final PasswordEncoder passwordEncoder;
    public  UserService(UserRepo userRepo,PasswordEncoder passwordEncoder){
        this.userRepo=userRepo;
        this.passwordEncoder=passwordEncoder;
    }

    public User getUser(Long id){
        return userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("User with this iD doesnt exist"));
    }
    public void deleteUser(Long id){
        userRepo.deleteById(id);
    }
    public void registerUser(UserRegistrationDto dto){
        User user = new User();
        user.setName(dto.getName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setSurname(dto.getSurname());
        userRepo.save(user);
    }



    public void updateUser(UserRequestDto user, Long id){
       User existingUser = userRepo.findById(id).orElseThrow();
       existingUser.setName(user.getName());
       existingUser.setEmail(user.getEmail());
       existingUser.setSurname(user.getSurname());
        userRepo.save(existingUser);

    }

}
