package com.example.HelpingHands.AuthenticationRequestsAndResponses;

import com.example.HelpingHands.Entity.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class VolunteerRegisterRequest extends RegisterRequest{
    private String fullName;
    private Gender gender;
    private Date birthdate;
}
