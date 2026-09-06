package com.example.HelpingHands.AuthenticationRequestsAndResponses;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String name;
    private String address;
    private String phone;
    private String role;


}
