package com.example.HelpingHands.AuthenticationRequestsAndResponses;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
@EqualsAndHashCode(callSuper = true)
@Data
public class OrganizationRegisterRequest extends RegisterRequest{

    private String description;
    private String type;
    private String founder;
    private Date foundedAt;

}
