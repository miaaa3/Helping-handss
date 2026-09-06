package com.example.HelpingHands.ServiceImpl;

import com.example.HelpingHands.DTO.AdminOrganizationResponse;
import com.example.HelpingHands.DTO.AdminPostResponse;
import com.example.HelpingHands.DTO.AdminUserResponse;
import com.example.HelpingHands.DTO.DonationResponse;
import com.example.HelpingHands.DTO.OpportunityResponse;
import com.example.HelpingHands.Entity.Organization;
import com.example.HelpingHands.Entity.OrganizationVerificationStatus;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Repository.DonationRepository;
import com.example.HelpingHands.Repository.OrganizationRepository;
import com.example.HelpingHands.Repository.PostRepository;
import com.example.HelpingHands.Repository.UserRepository;
import com.example.HelpingHands.Service.AdminService;
import com.example.HelpingHands.Service.OpportunityService;
import com.example.HelpingHands.Service.PostService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final PostRepository postRepository;
    private final PostService postService;
    private final OpportunityService opportunityService;
    private final DonationRepository donationRepository;

    @Override
    public List<AdminOrganizationResponse> listOrganizations() {
        return organizationRepository.findAll().stream()
                .map(AdminOrganizationResponse::fromEntity).toList();
    }

    @Override
    public AdminOrganizationResponse setOrganizationVerification(Long organizationId, OrganizationVerificationStatus status) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new EntityNotFoundException("Organization with id " + organizationId + " not found"));
        organization.setVerificationStatus(status);
        return AdminOrganizationResponse.fromEntity(organizationRepository.save(organization));
    }

    @Override
    public List<AdminUserResponse> listUsers() {
        return userRepository.findAll().stream()
                .map(AdminUserResponse::fromEntity).toList();
    }

    @Override
    public AdminUserResponse setUserEnabled(Long userId, boolean enabled) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));
        if ("ADMIN".equals(user.getRole())) {
            throw new IllegalStateException("Admin accounts cannot be disabled");
        }
        user.setEnabled(enabled);
        return AdminUserResponse.fromEntity(userRepository.save(user));
    }

    @Override
    public Page<AdminPostResponse> listPosts(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable).map(AdminPostResponse::fromEntity);
    }

    @Override
    public void deletePost(Long postId) {
        postService.deleteMediaPostsByPostId(postId);
        postService.deletePost(postId);
    }

    @Override
    public List<OpportunityResponse> listOpportunities() {
        return opportunityService.getAllOpportunitiesForAdmin();
    }

    @Override
    public void deleteOpportunity(Long opportunityId) {
        opportunityService.adminDeleteOpportunity(opportunityId);
    }

    @Override
    public Page<DonationResponse> listDonations(Pageable pageable) {
        return donationRepository.findAllByOrderByCreatedAtDesc(pageable).map(DonationResponse::fromEntity);
    }
}
