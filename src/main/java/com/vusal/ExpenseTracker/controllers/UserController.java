package com.vusal.ExpenseTracker.controllers;
import com.vusal.ExpenseTracker.Entity.User;
import com.vusal.ExpenseTracker.dto.UserRegistrationDto;
import com.vusal.ExpenseTracker.dto.UserRequestDto;
import com.vusal.ExpenseTracker.dto.UserResponseDto;
import com.vusal.ExpenseTracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable Long id){
        User user = userService.getUser(id);
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@Valid @RequestBody UserRegistrationDto user){
        userService.registerUser(user);
    }
    @PutMapping("/{id}")
    public void updateUser(@Valid @RequestBody UserRequestDto user,@PathVariable Long id){
        userService.updateUser(user,id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }

}