package com.example.HelpingHands.ServiceImpl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Repository.UserRepository;
import com.example.HelpingHands.Service.JWTService;
import com.example.HelpingHands.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

@Service
public class JWTServiceImpl implements JWTService {
    @Value("${secret.key}")
    private String secretKey;

    @Override
    public String generateToken(UserEntity userEntity) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        return JWT.create()
                .withSubject(userEntity.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + 48 * 60 * 60 * 1000))
                .withClaim("role",userEntity.getRole())
                .sign(algorithm);
    }

    @Override
    public String generateRefreshToken(UserEntity userEntity) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        return JWT.create().withSubject(userEntity.getName()).
                withExpiresAt(new Date(System.currentTimeMillis()+70*60*1000))
                .sign(algorithm);
    }
    /*@Override
    public String extractUsername(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getSubject();
    }
     @Override
    public UserEntity getUserFromToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        return userRepository.findByEmail(username).orElse(null);
    }*/
}
