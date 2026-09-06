package com.example.HelpingHands.DTO;

import com.example.HelpingHands.Entity.OrganizationVerificationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminVerificationRequest {
    private OrganizationVerificationStatus status;
}
