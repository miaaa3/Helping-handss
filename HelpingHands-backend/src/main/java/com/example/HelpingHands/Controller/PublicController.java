package com.example.HelpingHands.Controller;

import com.example.HelpingHands.DTO.OpportunityResponse;
import com.example.HelpingHands.DTO.PublicOrganizationResponse;
import com.example.HelpingHands.Entity.OrganizationVerificationStatus;
import com.example.HelpingHands.Repository.OpportunityRepository;
import com.example.HelpingHands.Repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Public endpoints accessible without authentication - used by the welcome page. */
@RequiredArgsConstructor
@RestController
@RequestMapping("public")
public class PublicController {

    private final OpportunityRepository opportunityRepository;
    private final OrganizationRepository organizationRepository;

    /** Open opportunities from verified organizations, soonest first. */
    @GetMapping("/opportunities")
    public ResponseEntity<List<OpportunityResponse>> getPublicOpportunities() {
        List<OpportunityResponse> result = opportunityRepository.findPublicOpportunities()
                .stream().map(OpportunityResponse::fromEntity).toList();
        return ResponseEntity.ok(result);
    }

    /** Verified organizations only. */
    @GetMapping("/organizations")
    public ResponseEntity<List<PublicOrganizationResponse>> getPublicOrganizations() {
        List<PublicOrganizationResponse> result = organizationRepository
                .findByVerificationStatus(OrganizationVerificationStatus.VERIFIED)
                .stream().map(PublicOrganizationResponse::fromEntity).toList();
        return ResponseEntity.ok(result);
    }
}
