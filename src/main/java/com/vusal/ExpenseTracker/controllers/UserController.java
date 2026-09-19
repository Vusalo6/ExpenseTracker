package com.vusal.ExpenseTracker.controllers;
import com.vusal.ExpenseTracker.Entity.User;
import com.vusal.ExpenseTracker.dto.UserRequestDto;
import com.vusal.ExpenseTracker.dto.UserResponseDto;
import com.vusal.ExpenseTracker.service.UserService;
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
    public void createUser(@RequestBody UserRequestDto user){
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setSurname(user.getSurname());
        userService.saveUser(newUser);
    }
    @PutMapping("/{id}")
    public void updateUser(@RequestBody UserRequestDto user,@PathVariable Long id){
        userService.updateUser(user,id);
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }

}