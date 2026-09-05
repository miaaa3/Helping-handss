package com.example.HelpingHands.ServiceImpl;

import com.example.HelpingHands.DTO.OpportunityResponse;
import com.example.HelpingHands.Entity.Opportunity;
import com.example.HelpingHands.Entity.OpportunityCategory;
import com.example.HelpingHands.Entity.OpportunityStatus;
import com.example.HelpingHands.Entity.Organization;
import com.example.HelpingHands.Entity.OrganizationVerificationStatus;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Repository.OpportunityRepository;
import com.example.HelpingHands.Repository.OrganizationRepository;
import com.example.HelpingHands.Repository.UserRepository;
import com.example.HelpingHands.Service.FollowService;
import com.example.HelpingHands.Service.OpportunityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OpportunityServiceImpl implements OpportunityService {

    private final OpportunityRepository opportunityRepository;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final FollowService followService;

    @Override
    public OpportunityResponse createOpportunity(Opportunity opportunity, String organizationEmail) {
        Organization organization = getOrganizationByEmail(organizationEmail);
        if (organization.getVerificationStatus() != OrganizationVerificationStatus.VERIFIED) {
            throw new IllegalStateException("Your organization must be verified by an admin before you can post opportunities.");
        }
        opportunity.setOrganization(organization);
        if (opportunity.getStatus() == null) {
            opportunity.setStatus(OpportunityStatus.DRAFT);
        }
        return OpportunityResponse.fromEntity(opportunityRepository.save(opportunity));
    }

    @Override
    public OpportunityResponse getOpportunityById(Long opportunityId) {
        return OpportunityResponse.fromEntity(getOpportunityOrThrow(opportunityId));
    }

    @Override
    public OpportunityResponse updateOpportunity(Long opportunityId, Opportunity updated, String organizationEmail) {
        Opportunity existing = getOpportunityOrThrow(opportunityId);
        requireOwner(existing, organizationEmail);

        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setCategory(updated.getCategory());
        existing.setDate(updated.getDate());
        existing.setLocation(updated.getLocation());
        existing.setNeededVolunteers(updated.getNeededVolunteers());
        if (updated.getStatus() != null) {
            existing.setStatus(updated.getStatus());
        }
        return OpportunityResponse.fromEntity(opportunityRepository.save(existing));
    }

    @Override
    public void deleteOpportunity(Long opportunityId, String organizationEmail) {
        Opportunity existing = getOpportunityOrThrow(opportunityId);
        requireOwner(existing, organizationEmail);
        opportunityRepository.deleteById(opportunityId);
    }

    @Override
    public List<OpportunityResponse> searchOpportunities(OpportunityCategory category, OpportunityStatus status, String location) {
        // Public browsing never surfaces drafts, regardless of the requested status filter -
        // drafts are only visible to their owning organization via getOpportunitiesByOrganization.
        List<Opportunity> results = opportunityRepository.search(category, status, location).stream()
                .filter(o -> o.getStatus() != OpportunityStatus.DRAFT)
                .toList();
        return results.stream().map(OpportunityResponse::fromEntity).toList();
    }

    @Override
    public List<OpportunityResponse> getOpportunitiesByOrganization(Long organizationId, String requesterEmail) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new EntityNotFoundException("Organization with ID " + organizationId + " not found"));

        boolean isOwner = organization.getEmail().equals(requesterEmail);
        return opportunityRepository.findByOrganizationOrderByDateAsc(organization).stream()
                .filter(o -> isOwner || o.getStatus() != OpportunityStatus.DRAFT)
                .map(OpportunityResponse::fromEntity).toList();
    }

    @Override
    public List<OpportunityResponse> getOpportunitiesForFollowed(Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        // Admin sees all public opportunities in the feed (they follow nobody by design).
        if (user != null && "ADMIN".equals(user.getRole())) {
            return opportunityRepository.findPublicOpportunities().stream()
                    .map(OpportunityResponse::fromEntity).toList();
        }
        List<UserEntity> followedUsers = followService.getFollowing(userId);
        if (followedUsers.isEmpty()) {
            return List.of();
        }
        return opportunityRepository.findVisibleByOrganizationIn(followedUsers).stream()
                .map(OpportunityResponse::fromEntity).toList();
    }

    @Override
    public List<OpportunityResponse> getAllOpportunitiesForAdmin() {
        return opportunityRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(OpportunityResponse::fromEntity).toList();
    }

    @Override
    public void adminDeleteOpportunity(Long opportunityId) {
        getOpportunityOrThrow(opportunityId);
        opportunityRepository.deleteById(opportunityId);
    }

    private Opportunity getOpportunityOrThrow(Long opportunityId) {
        return opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new IllegalArgumentException("Opportunity with id " + opportunityId + " not found"));
    }

    private Organization getOrganizationByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!(user instanceof Organization organization)) {
            throw new IllegalStateException("Only organizations can manage opportunities");
        }
        return organization;
    }

    private void requireOwner(Opportunity opportunity, String organizationEmail) {
        if (opportunity.getOrganization() == null
                || !opportunity.getOrganization().getEmail().equals(organizationEmail)) {
            throw new IllegalStateException("You do not have permission to modify this opportunity");
        }
    }
}
