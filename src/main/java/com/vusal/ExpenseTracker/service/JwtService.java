package com.vusal.ExpenseTracker.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {
    JwtEncoder jwtEncoder;
    public  JwtService(JwtEncoder jwtEncoder){
        this.jwtEncoder=jwtEncoder;
    }

    public String generateToken(Authentication authentication){
        Instant now = Instant.now();
        JwtClaimsSet claimsSet=JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .subject(authentication.getName())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }
}
