package com.example.HelpingHands.DTO;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Editable profile fields for an organization. Deliberately excludes
 * email/password/role so a user can't escalate privileges or take
 * over another account via the settings form.
 */
@Data
public class OrganizationUpdateRequest {
    private String phone;
    private String address;
    private String profile;
    private String bio;
    private String description;
    private String website;
    private String type;
    private String founder;
    private BigDecimal campaignGoal;
}
