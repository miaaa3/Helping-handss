package com.example.HelpingHands.DTO;

import lombok.Data;

import java.util.Set;

/**
 * Editable profile fields for a volunteer. Deliberately excludes
 * email/password/role so a user can't escalate privileges or take
 * over another account via the settings form.
 */
@Data
public class VolunteerUpdateRequest {
    private String fullName;
    private String phone;
    private String address;
    private String profile;
    private String bio;
    private Set<String> interests;
}
