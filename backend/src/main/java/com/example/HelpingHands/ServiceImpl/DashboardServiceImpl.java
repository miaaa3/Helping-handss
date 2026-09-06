package com.example.HelpingHands.ServiceImpl;

import com.example.HelpingHands.DTO.ApplicationResponse;
import com.example.HelpingHands.DTO.OpportunityResponse;
import com.example.HelpingHands.DTO.OrganizationDashboardResponse;
import com.example.HelpingHands.DTO.VolunteerDashboardResponse;
import com.example.HelpingHands.Entity.ApplicationStatus;
import com.example.HelpingHands.Entity.Organization;
import com.example.HelpingHands.Entity.OpportunityStatus;
import com.example.HelpingHands.Entity.Volunteer;
import com.example.HelpingHands.Repository.DonationRepository;
import com.example.HelpingHands.Repository.MessageRepository;
import com.example.HelpingHands.Repository.OpportunityApplicationRepository;
import com.example.HelpingHands.Repository.OpportunityRepository;
import com.example.HelpingHands.Repository.OrganizationRepository;
import com.example.HelpingHands.Repository.PostRepository;
import com.example.HelpingHands.Repository.UserRepository;
import com.example.HelpingHands.Repository.VolunteerRepository;
import com.example.HelpingHands.Service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class DashboardServiceImpl implements DashboardService {

    private final OpportunityApplicationRepository applicationRepository;
    private final OpportunityRepository opportunityRepository;
    private final VolunteerRepository volunteerRepository;
    private final OrganizationRepository organizationRepository;
    private final MessageRepository messageRepository;
    private final DonationRepository donationRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public VolunteerDashboardResponse getVolunteerDashboard(String volunteerEmail) {
        Volunteer volunteer = volunteerRepository.findByEmail(volunteerEmail)
                .orElseThrow(() -> new IllegalStateException("Volunteer not found"));
        Long volunteerId = volunteer.getId();

        VolunteerDashboardResponse dashboard = new VolunteerDashboardResponse();
        dashboard.setPendingApplications(applicationRepository.countByVolunteerIdAndStatus(volunteerId, ApplicationStatus.PENDING));
        dashboard.setAcceptedApplications(applicationRepository.countByVolunteerIdAndStatus(volunteerId, ApplicationStatus.ACCEPTED));
        dashboard.setRejectedApplications(applicationRepository.countByVolunteerIdAndStatus(volunteerId, ApplicationStatus.REJECTED));
        dashboard.setCompletedOpportunities(applicationRepository.countByVolunteerIdAndStatusAndOpportunityStatus(
                volunteerId, ApplicationStatus.ACCEPTED, OpportunityStatus.CLOSED));
        dashboard.setUnreadMessages(messageRepository.countByReceiverIdAndIsReadFalse(volunteerId));

        dashboard.setApplications(applicationRepository.findByVolunteerIdOrderByCreatedAtDesc(volunteerId).stream()
                .map(ApplicationResponse::fromEntity).toList());

        dashboard.setUpcomingOpportunities(applicationRepository
                .findUpcomingByVolunteerAndStatus(volunteerId, ApplicationStatus.ACCEPTED, LocalDateTime.now()).stream()
                .map(application -> OpportunityResponse.fromEntity(application.getOpportunity())).toList());

        return dashboard;
    }

    @Override
    public OrganizationDashboardResponse getOrganizationDashboard(String organizationEmail) {
        Organization organization = organizationRepository.findByEmail(organizationEmail)
                .orElseThrow(() -> new IllegalStateException("Organization not found"));
        Long organizationId = organization.getId();

        OrganizationDashboardResponse dashboard = new OrganizationDashboardResponse();
        dashboard.setOpportunities(opportunityRepository.findByOrganizationOrderByDateAsc(organization).stream()
                .map(OpportunityResponse::fromEntity).toList());
        dashboard.setPendingApplicantsCount(applicationRepository.countByOpportunityOrganizationIdAndStatus(
                organizationId, ApplicationStatus.PENDING));
        dashboard.setUnreadMessages(messageRepository.countByReceiverIdAndIsReadFalse(organizationId));
        dashboard.setTotalRaised(donationRepository.getTotalRaisedForOrganization(organizationId));
        dashboard.setFollowerCount(userRepository.numberOfFollowers(organizationId));
        dashboard.setPostCount(postRepository.countByUserId(organizationId));

        return dashboard;
    }
}
