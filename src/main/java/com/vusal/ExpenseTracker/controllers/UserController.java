package com.vusal.ExpenseTracker.controllers;
import com.vusal.ExpenseTracker.Entity.User;
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
    public User getUser(@PathVariable Long id){
        return userService.getUser(id);
    }
    @PostMapping
    public void createUser(@RequestBody User user){
        userService.createUser(user);
    }
    @PutMapping("/{id}")
    public void updateUser(@RequestBody User user,@PathVariable Long id){
        userService.updateUser(user,id);
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }

}