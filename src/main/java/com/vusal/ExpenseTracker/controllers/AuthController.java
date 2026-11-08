package com.vusal.ExpenseTracker.controllers;

import com.vusal.ExpenseTracker.dto.LoginRequestDto;
import com.vusal.ExpenseTracker.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

  @RestController
  @RequestMapping("/auth")
  public class AuthController {
     private final  AuthService authService;
     public AuthController(AuthService authService){
        this.authService=authService;
    }
    @PostMapping("/login")
     public String  login(@Valid @RequestBody LoginRequestDto loginRequestDto){
       return authService.authentication(loginRequestDto);

    }
}
