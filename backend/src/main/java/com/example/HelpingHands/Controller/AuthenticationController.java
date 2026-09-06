package com.example.HelpingHands.Controller;

import com.example.HelpingHands.AuthenticationRequestsAndResponses.AuthenticationRequest;
import com.example.HelpingHands.AuthenticationRequestsAndResponses.OrganizationRegisterRequest;
import com.example.HelpingHands.AuthenticationRequestsAndResponses.RegisterRequest;
import com.example.HelpingHands.AuthenticationRequestsAndResponses.VolunteerRegisterRequest;
import com.example.HelpingHands.Service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthenticationController {
   private final AuthenticationService authenticationService;

    @PostMapping("/register/volunteer")
    public ResponseEntity<?> registerVolunteer(@RequestBody VolunteerRegisterRequest registerRequest){
            return ResponseEntity.ok(authenticationService.register(registerRequest));
    }
    @PostMapping("/register/organization")
    public ResponseEntity<?> registerOrganization(@RequestBody OrganizationRegisterRequest registerRequest){
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest){
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully");
    }
}
