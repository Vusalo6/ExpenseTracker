package com.vusal.ExpenseTracker.service;

import com.vusal.ExpenseTracker.dto.LoginRequestDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
   private final AuthenticationManager authenticationManager;
   private final JwtService jwtService;

    AuthService(AuthenticationManager authenticationManager,JwtService jwtService){
        this.authenticationManager=authenticationManager;
        this.jwtService=jwtService;
    }
    public String authentication(LoginRequestDto dto){
     Authentication authentication=  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(),dto.getPassword()));
     return jwtService.generateToken(authentication);
    }


}
