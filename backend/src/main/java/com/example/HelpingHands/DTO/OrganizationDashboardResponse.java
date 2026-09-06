package com.example.HelpingHands.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Summary data for an organization's dashboard: posted opportunities,
 * pending applicants, donation totals, follower and post counts.
 */
@Data
@NoArgsConstructor
public class OrganizationDashboardResponse {
    private List<OpportunityResponse> opportunities;
    private long pendingApplicantsCount;
    private long unreadMessages;
    private BigDecimal totalRaised;
    private int followerCount;
    private long postCount;
}
