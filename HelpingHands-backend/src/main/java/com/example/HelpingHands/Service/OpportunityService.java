package com.example.HelpingHands.Service;

import com.example.HelpingHands.DTO.OpportunityResponse;
import com.example.HelpingHands.Entity.Opportunity;
import com.example.HelpingHands.Entity.OpportunityCategory;
import com.example.HelpingHands.Entity.OpportunityStatus;

import java.util.List;

public interface OpportunityService {

    /** Creates an opportunity owned by the organization identified by {@code organizationEmail}. */
    OpportunityResponse createOpportunity(Opportunity opportunity, String organizationEmail);

    OpportunityResponse getOpportunityById(Long opportunityId);

    /** Updates an opportunity, only if it belongs to {@code organizationEmail}. */
    OpportunityResponse updateOpportunity(Long opportunityId, Opportunity updated, String organizationEmail);

    /** Deletes an opportunity, only if it belongs to {@code organizationEmail}. */
    void deleteOpportunity(Long opportunityId, String organizationEmail);

    /** Browse/search opportunities. Any filter may be null. Drafts are always excluded. */
    List<OpportunityResponse> searchOpportunities(OpportunityCategory category, OpportunityStatus status, String location);

    /** Opportunities posted by one organization. Drafts are only included if {@code requesterEmail} owns the organization. */
    List<OpportunityResponse> getOpportunitiesByOrganization(Long organizationId, String requesterEmail);

    /** Open/full/closed opportunities from organizations the given user follows, for feed cards. */
    List<OpportunityResponse> getOpportunitiesForFollowed(Long userId);

    /** All opportunities platform-wide, including drafts, newest first - for admin moderation. */
    List<OpportunityResponse> getAllOpportunitiesForAdmin();

    /** Deletes any opportunity regardless of ownership - for admin moderation. */
    void adminDeleteOpportunity(Long opportunityId);
}
