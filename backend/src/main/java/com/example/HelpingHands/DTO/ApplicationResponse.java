package com.example.HelpingHands.DTO;

import com.example.HelpingHands.Entity.ApplicationStatus;
import com.example.HelpingHands.Entity.OpportunityApplication;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * A volunteer's application, with enough context about the opportunity and
 * volunteer to render either dashboard without extra lookups.
 */
@Data
@NoArgsConstructor
public class ApplicationResponse {
    private Long id;
    private ApplicationStatus status;
    private LocalDateTime createdAt;

    private OpportunityResponse opportunity;

    private Long volunteerId;
    private String volunteerName;
    private String volunteerProfile;
    private String volunteerEmail;

    public static ApplicationResponse fromEntity(OpportunityApplication application) {
        ApplicationResponse dto = new ApplicationResponse();
        dto.setId(application.getId());
        dto.setStatus(application.getStatus());
        dto.setCreatedAt(application.getCreatedAt());
        dto.setOpportunity(OpportunityResponse.fromEntity(application.getOpportunity()));
        if (application.getVolunteer() != null) {
            dto.setVolunteerId(application.getVolunteer().getId());
            dto.setVolunteerName(application.getVolunteer().getName());
            dto.setVolunteerProfile(application.getVolunteer().getProfile());
            dto.setVolunteerEmail(application.getVolunteer().getEmail());
        }
        return dto;
    }
}
