package com.example.HelpingHands.Controller;

import com.example.HelpingHands.DTO.OpportunityResponse;
import com.example.HelpingHands.Entity.Opportunity;
import com.example.HelpingHands.Entity.OpportunityCategory;
import com.example.HelpingHands.Entity.OpportunityStatus;
import com.example.HelpingHands.Service.OpportunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/opportunities")
public class OpportunityController {

    private final OpportunityService opportunityService;

    /** Browse/filter opportunities. Without a status filter, drafts are hidden. */
    @GetMapping
    public ResponseEntity<List<OpportunityResponse>> search(@RequestParam(required = false) OpportunityCategory category,
                                                              @RequestParam(required = false) OpportunityStatus status,
                                                              @RequestParam(required = false) String location) {
        return ResponseEntity.ok(opportunityService.searchOpportunities(category, status, location));
    }

    @GetMapping("/{opportunityId}")
    public ResponseEntity<OpportunityResponse> getById(@PathVariable Long opportunityId) {
        return ResponseEntity.ok(opportunityService.getOpportunityById(opportunityId));
    }

    /** Opportunities posted by one organization. Drafts are only included for the owning organization. */
    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<OpportunityResponse>> getByOrganization(@PathVariable Long organizationId, Principal principal) {
        return ResponseEntity.ok(opportunityService.getOpportunitiesByOrganization(organizationId, principal.getName()));
    }

    /** Opportunities from organizations the current user follows - for feed cards. */
    @GetMapping("/feed")
    public ResponseEntity<List<OpportunityResponse>> getForFeed(@RequestParam Long userId) {
        return ResponseEntity.ok(opportunityService.getOpportunitiesForFollowed(userId));
    }

    @PostMapping
    public ResponseEntity<OpportunityResponse> create(@RequestBody Opportunity opportunity, Principal principal) {
        OpportunityResponse created = opportunityService.createOpportunity(opportunity, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{opportunityId}")
    public ResponseEntity<OpportunityResponse> update(@PathVariable Long opportunityId,
                                                        @RequestBody Opportunity opportunity,
                                                        Principal principal) {
        return ResponseEntity.ok(opportunityService.updateOpportunity(opportunityId, opportunity, principal.getName()));
    }

    @DeleteMapping("/{opportunityId}")
    public ResponseEntity<Void> delete(@PathVariable Long opportunityId, Principal principal) {
        opportunityService.deleteOpportunity(opportunityId, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
