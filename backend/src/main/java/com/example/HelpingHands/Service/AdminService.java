package com.example.HelpingHands.Service;

import com.example.HelpingHands.DTO.AdminOrganizationResponse;
import com.example.HelpingHands.DTO.AdminPostResponse;
import com.example.HelpingHands.DTO.AdminUserResponse;
import com.example.HelpingHands.DTO.DonationResponse;
import com.example.HelpingHands.DTO.OpportunityResponse;
import com.example.HelpingHands.Entity.OrganizationVerificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/** Backing service for the admin panel: organization verification, user management, content moderation, donation activity. */
public interface AdminService {

    List<AdminOrganizationResponse> listOrganizations();

    AdminOrganizationResponse setOrganizationVerification(Long organizationId, OrganizationVerificationStatus status);

    List<AdminUserResponse> listUsers();

    /** Enables/disables a user account. Admin accounts cannot be disabled this way. */
    AdminUserResponse setUserEnabled(Long userId, boolean enabled);

    Page<AdminPostResponse> listPosts(Pageable pageable);

    void deletePost(Long postId);

    List<OpportunityResponse> listOpportunities();

    void deleteOpportunity(Long opportunityId);

    Page<DonationResponse> listDonations(Pageable pageable);
}
