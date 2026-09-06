package com.example.HelpingHands.Service;

import com.example.HelpingHands.DTO.ApplicationResponse;

import java.util.List;

public interface ApplicationService {

    /** A volunteer applies to an open opportunity. Fails if already applied (PENDING/ACCEPTED) or opportunity isn't open. */
    ApplicationResponse apply(Long opportunityId, String volunteerEmail);

    /** A volunteer withdraws their own application. */
    ApplicationResponse withdraw(Long applicationId, String volunteerEmail);

    /** All applications submitted by the requesting volunteer - for the volunteer dashboard. */
    List<ApplicationResponse> getMyApplications(String volunteerEmail);

    /** All applicants for an opportunity - only visible to the owning organization. */
    List<ApplicationResponse> getApplicantsForOpportunity(Long opportunityId, String organizationEmail);

    /** Organization accepts or rejects a pending application. */
    ApplicationResponse decide(Long applicationId, String organizationEmail, boolean accept);
}
