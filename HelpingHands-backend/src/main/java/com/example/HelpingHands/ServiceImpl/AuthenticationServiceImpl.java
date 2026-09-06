package com.example.HelpingHands.ServiceImpl;

import com.example.HelpingHands.AuthenticationRequestsAndResponses.*;
import com.example.HelpingHands.Entity.Follow;
import com.example.HelpingHands.Entity.Organization;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Entity.Volunteer;
import com.example.HelpingHands.Repository.UserRepository;
import com.example.HelpingHands.Service.AuthenticationService;
import com.example.HelpingHands.Service.FollowService;
import com.example.HelpingHands.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final JWTServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final FollowService followService;

    @Override
    public ResponseEntity<?> register(VolunteerRegisterRequest registerRequest) {

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent() || userRepository.findByName(registerRequest.getName()).isPresent()){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User with this email or username already exists.");
        }
            UserEntity createdUserEntity = userService.createVolunteer(new Volunteer(registerRequest.getEmail(),
                                                                registerRequest.getPassword(),
                                                                registerRequest.getAddress(),
                                                                registerRequest.getPhone(),
                                                                "VOLUNTEER",
                                                                registerRequest.getName(),
                                                                registerRequest.getFullName(),
                                                                registerRequest.getGender(),
                                                                registerRequest.getBirthdate()));

        Follow selfFollow = followService.createFollow(createdUserEntity, createdUserEntity);

        String jwtAccessToken = jwtService.generateToken(createdUserEntity);
        return ResponseEntity.ok(AuthenticationResponse.builder()
                            .id(createdUserEntity.getId())
                            .email(createdUserEntity.getEmail())
                            .access_token(jwtAccessToken)
                            .role(createdUserEntity.getRole()).build());
    }

    @Override
    public ResponseEntity<?> register(OrganizationRegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()|| userRepository.findByName(registerRequest.getName()).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with this email or username already exists.");
        }
        UserEntity createdUserEntity = userService.createOrganization(
                    new Organization(registerRequest.getEmail(),
                                    registerRequest.getPassword(),
                                    registerRequest.getAddress(),
                                    registerRequest.getPhone(),
                                    "ORGANIZATION",
                                    registerRequest.getName(),
                                    registerRequest.getDescription(),
                                    registerRequest.getType(),
                                    registerRequest.getFounder(),
                                    registerRequest.getFoundedAt()));

        Follow selfFollow = followService.createFollow(createdUserEntity, createdUserEntity);

        String jwtAccessToken = jwtService.generateToken(createdUserEntity);
        return ResponseEntity.ok(AuthenticationResponse.builder()
                .id(createdUserEntity.getId())
                .email(createdUserEntity.getEmail())
                .access_token(jwtAccessToken)
                .role(createdUserEntity.getRole()).build());
    }


    @Override
    public ResponseEntity<?> authenticate(AuthenticationRequest authenticationRequest) {
        try {
            UserEntity userEntity = userRepository.findByEmail(authenticationRequest.getEmail())
                    .orElseThrow(() -> new NoSuchElementException("USER not found"));

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(),
                    authenticationRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwtAccessToken = jwtService.generateToken(userEntity );
            return ResponseEntity.ok(AuthenticationResponse.builder()
                    .id(userEntity.getId())
                    .access_token(jwtAccessToken)
                    .email(userEntity.getEmail())
                    .role(userEntity.getRole()).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Invalid credential: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
