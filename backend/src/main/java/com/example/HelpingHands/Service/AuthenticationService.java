package com.example.HelpingHands.Service;

import com.example.HelpingHands.AuthenticationRequestsAndResponses.AuthenticationRequest;
import com.example.HelpingHands.AuthenticationRequestsAndResponses.OrganizationRegisterRequest;
import com.example.HelpingHands.AuthenticationRequestsAndResponses.RegisterRequest;
import com.example.HelpingHands.AuthenticationRequestsAndResponses.VolunteerRegisterRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    public ResponseEntity<?> register(VolunteerRegisterRequest registerRequest);
    public ResponseEntity<?> register(OrganizationRegisterRequest registerRequest);

    public ResponseEntity<?> authenticate(AuthenticationRequest authenticationRequest);

}
