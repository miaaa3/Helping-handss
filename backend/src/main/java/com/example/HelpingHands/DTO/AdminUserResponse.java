package com.example.HelpingHands.DTO;

import com.example.HelpingHands.Entity.Organization;
import com.example.HelpingHands.Entity.OrganizationVerificationStatus;
import com.example.HelpingHands.Entity.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Row shape for the admin "manage users" table - covers volunteers, organizations, and admins. */
@Data
@NoArgsConstructor
public class AdminUserResponse {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String profile;
    private boolean enabled;

    /** Organizations only. */
    private OrganizationVerificationStatus verificationStatus;

    public static AdminUserResponse fromEntity(UserEntity user) {
        AdminUserResponse dto = new AdminUserResponse();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setProfile(user.getProfile());
        dto.setEnabled(user.isEnabled());
        if (user instanceof Organization organization) {
            dto.setVerificationStatus(organization.getVerificationStatus());
        }
        return dto;
    }
}
