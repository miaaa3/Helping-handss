package com.example.HelpingHands.DTO;

import com.example.HelpingHands.Entity.Opportunity;
import com.example.HelpingHands.Entity.OpportunityCategory;
import com.example.HelpingHands.Entity.OpportunityStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class OpportunityResponse {
    private Long id;
    private String title;
    private String description;
    private OpportunityCategory category;
    private LocalDateTime date;
    private String location;
    private int neededVolunteers;
    private int applicantCount;
    private OpportunityStatus status;
    private LocalDateTime createdAt;
    private Long organizationId;
    private String organizationName;
    private String organizationProfile;

    public static OpportunityResponse fromEntity(Opportunity opportunity) {
        OpportunityResponse dto = new OpportunityResponse();
        dto.setId(opportunity.getId());
        dto.setTitle(opportunity.getTitle());
        dto.setDescription(opportunity.getDescription());
        dto.setCategory(opportunity.getCategory());
        dto.setDate(opportunity.getDate());
        dto.setLocation(opportunity.getLocation());
        dto.setNeededVolunteers(opportunity.getNeededVolunteers());
        dto.setApplicantCount(opportunity.getApplications() != null ? opportunity.getApplications().size() : 0);
        dto.setStatus(opportunity.getStatus());
        dto.setCreatedAt(opportunity.getCreatedAt());
        if (opportunity.getOrganization() != null) {
            dto.setOrganizationId(opportunity.getOrganization().getId());
            dto.setOrganizationName(opportunity.getOrganization().getName());
            dto.setOrganizationProfile(opportunity.getOrganization().getProfile());
        }
        return dto;
    }
}
