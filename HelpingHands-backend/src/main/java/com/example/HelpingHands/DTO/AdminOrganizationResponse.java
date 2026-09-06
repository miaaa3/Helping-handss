package com.example.HelpingHands.DTO;

import com.example.HelpingHands.Entity.Organization;
import com.example.HelpingHands.Entity.OrganizationVerificationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/** Row shape for the admin "verify organizations" table. */
@Data
@NoArgsConstructor
public class AdminOrganizationResponse {
    private Long id;
    private String name;
    private String email;
    private String profile;
    private String type;
    private boolean enabled;
    private OrganizationVerificationStatus verificationStatus;
    private BigDecimal campaignGoal;

    public static AdminOrganizationResponse fromEntity(Organization organization) {
        AdminOrganizationResponse dto = new AdminOrganizationResponse();
        dto.setId(organization.getId());
        dto.setName(organization.getName());
        dto.setEmail(organization.getEmail());
        dto.setProfile(organization.getProfile());
        dto.setType(organization.getType());
        dto.setEnabled(organization.isEnabled());
        dto.setVerificationStatus(organization.getVerificationStatus());
        dto.setCampaignGoal(organization.getCampaignGoal());
        return dto;
    }
}
