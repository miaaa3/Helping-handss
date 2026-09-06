package com.example.HelpingHands.Service;

import com.example.HelpingHands.DTO.OrganizationDashboardResponse;
import com.example.HelpingHands.DTO.VolunteerDashboardResponse;

public interface DashboardService {

    /** Aggregated summary for the volunteer dashboard (applications, impact stats, upcoming opportunities). */
    VolunteerDashboardResponse getVolunteerDashboard(String volunteerEmail);

    /** Aggregated summary for the organization dashboard (opportunities, applicants, donations, reach). */
    OrganizationDashboardResponse getOrganizationDashboard(String organizationEmail);
}
