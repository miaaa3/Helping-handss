package com.example.HelpingHands.Service;

import com.example.HelpingHands.Entity.UserEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public interface JWTService {
     String generateToken(UserEntity userEntity);
     String generateRefreshToken(UserEntity userEntity);
     //String extractUsername(String token);
     // UserEntity getUserFromToken(String token);
}