package com.example.HelpingHands.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Summary data for a volunteer's dashboard: application counts, impact stats,
 * recent applications and upcoming accepted opportunities.
 */
@Data
@NoArgsConstructor
public class VolunteerDashboardResponse {
    private long pendingApplications;
    private long acceptedApplications;
    private long rejectedApplications;
    private long completedOpportunities;
    private long unreadMessages;
    private List<ApplicationResponse> applications;
    private List<OpportunityResponse> upcomingOpportunities;
}
