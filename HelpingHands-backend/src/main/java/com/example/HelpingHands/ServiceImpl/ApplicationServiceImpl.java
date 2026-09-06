package com.example.HelpingHands.ServiceImpl;

import com.example.HelpingHands.DTO.ApplicationResponse;
import com.example.HelpingHands.Entity.ApplicationStatus;
import com.example.HelpingHands.Entity.Opportunity;
import com.example.HelpingHands.Entity.OpportunityApplication;
import com.example.HelpingHands.Entity.OpportunityStatus;
import com.example.HelpingHands.Entity.Volunteer;
import com.example.HelpingHands.Repository.OpportunityApplicationRepository;
import com.example.HelpingHands.Repository.OpportunityRepository;
import com.example.HelpingHands.Repository.VolunteerRepository;
import com.example.HelpingHands.Service.ApplicationService;
import com.example.HelpingHands.Service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final OpportunityApplicationRepository applicationRepository;
    private final OpportunityRepository opportunityRepository;
    private final VolunteerRepository volunteerRepository;
    private final NotificationService notificationService;

    @Override
    public ApplicationResponse apply(Long opportunityId, String volunteerEmail) {
        Volunteer volunteer = getVolunteerByEmail(volunteerEmail);
        Opportunity opportunity = getOpportunityOrThrow(opportunityId);

        if (opportunity.getStatus() != OpportunityStatus.OPEN) {
            throw new IllegalStateException("This opportunity is not open for applications");
        }
        boolean alreadyApplied = applicationRepository.existsByOpportunityAndVolunteerAndStatusIn(
                opportunityId, volunteer.getId(), List.of(ApplicationStatus.PENDING, ApplicationStatus.ACCEPTED));
        if (alreadyApplied) {
            throw new IllegalStateException("You have already applied to this opportunity");
        }

        OpportunityApplication application = applicationRepository.save(new OpportunityApplication(opportunity, volunteer));
        notificationService.createApplicationNotification(volunteer, application);
        return ApplicationResponse.fromEntity(application);
    }

    @Override
    public ApplicationResponse withdraw(Long applicationId, String volunteerEmail) {
        OpportunityApplication application = getApplicationOrThrow(applicationId);
        requireOwnerVolunteer(application, volunteerEmail);

        if (application.getStatus() == ApplicationStatus.CANCELLED) {
            throw new IllegalStateException("This application has already been withdrawn");
        }
        application.setStatus(ApplicationStatus.CANCELLED);
        return ApplicationResponse.fromEntity(applicationRepository.save(application));
    }

    @Override
    public List<ApplicationResponse> getMyApplications(String volunteerEmail) {
        Volunteer volunteer = getVolunteerByEmail(volunteerEmail);
        return applicationRepository.findByVolunteerIdOrderByCreatedAtDesc(volunteer.getId()).stream()
                .map(ApplicationResponse::fromEntity).toList();
    }

    @Override
    public List<ApplicationResponse> getApplicantsForOpportunity(Long opportunityId, String organizationEmail) {
        Opportunity opportunity = getOpportunityOrThrow(opportunityId);
        requireOwnerOrganization(opportunity, organizationEmail);
        return applicationRepository.findByOpportunityIdOrderByCreatedAtDesc(opportunityId).stream()
                .map(ApplicationResponse::fromEntity).toList();
    }

    @Override
    public ApplicationResponse decide(Long applicationId, String organizationEmail, boolean accept) {
        OpportunityApplication application = getApplicationOrThrow(applicationId);
        requireOwnerOrganization(application.getOpportunity(), organizationEmail);

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new IllegalStateException("This application has already been decided");
        }
        application.setStatus(accept ? ApplicationStatus.ACCEPTED : ApplicationStatus.REJECTED);
        application = applicationRepository.save(application);
        notificationService.createApplicationDecisionNotification(application);
        return ApplicationResponse.fromEntity(application);
    }

    private Volunteer getVolunteerByEmail(String email) {
        return volunteerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Only volunteers can apply to opportunities"));
    }

    private Opportunity getOpportunityOrThrow(Long opportunityId) {
        return opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new EntityNotFoundException("Opportunity with id " + opportunityId + " not found"));
    }

    private OpportunityApplication getApplicationOrThrow(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("Application with id " + applicationId + " not found"));
    }

    private void requireOwnerVolunteer(OpportunityApplication application, String volunteerEmail) {
        if (!application.getVolunteer().getEmail().equals(volunteerEmail)) {
            throw new IllegalStateException("You do not have permission to modify this application");
        }
    }

    private void requireOwnerOrganization(Opportunity opportunity, String organizationEmail) {
        if (opportunity.getOrganization() == null || !opportunity.getOrganization().getEmail().equals(organizationEmail)) {
            throw new IllegalStateException("You do not have permission to manage this opportunity's applications");
        }
    }
}
