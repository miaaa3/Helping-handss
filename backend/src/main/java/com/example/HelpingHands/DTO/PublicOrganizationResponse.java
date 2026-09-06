package com.example.HelpingHands.DTO;

import com.example.HelpingHands.Entity.Organization;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Organization data visible to unauthenticated users on the public discovery page. */
@Data
@NoArgsConstructor
public class PublicOrganizationResponse {
    private Long id;
    private String name;
    private String profile;
    private String description;
    private String type;
    private String founder;
    private String website;
    private String address;

    public static PublicOrganizationResponse fromEntity(Organization org) {
        PublicOrganizationResponse dto = new PublicOrganizationResponse();
        dto.setId(org.getId());
        dto.setName(org.getName());
        dto.setProfile(org.getProfile());
        dto.setDescription(org.getDescription());
        dto.setType(org.getType());
        dto.setFounder(org.getFounder());
        dto.setWebsite(org.getWebsite());
        dto.setAddress(org.getAddress());
        return dto;
    }
}
