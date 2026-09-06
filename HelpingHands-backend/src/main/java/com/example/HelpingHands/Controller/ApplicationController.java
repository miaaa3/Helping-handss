package com.example.HelpingHands.Controller;

import com.example.HelpingHands.DTO.ApplicationResponse;
import com.example.HelpingHands.Service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    /** Volunteer applies to an open opportunity. */
    @PostMapping("/apply")
    public ResponseEntity<ApplicationResponse> apply(@RequestParam Long opportunityId, Principal principal) {
        ApplicationResponse response = applicationService.apply(opportunityId, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /** Volunteer withdraws their own application. */
    @DeleteMapping("/{applicationId}")
    public ResponseEntity<ApplicationResponse> withdraw(@PathVariable Long applicationId, Principal principal) {
        return ResponseEntity.ok(applicationService.withdraw(applicationId, principal.getName()));
    }

    /** The current volunteer's own applications - for the volunteer dashboard. */
    @GetMapping("/my")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications(Principal principal) {
        return ResponseEntity.ok(applicationService.getMyApplications(principal.getName()));
    }

    /** All applicants for one opportunity - only visible to the owning organization. */
    @GetMapping("/opportunity/{opportunityId}")
    public ResponseEntity<List<ApplicationResponse>> getApplicants(@PathVariable Long opportunityId, Principal principal) {
        return ResponseEntity.ok(applicationService.getApplicantsForOpportunity(opportunityId, principal.getName()));
    }

    /** Organization accepts or rejects a pending application. */
    @PutMapping("/{applicationId}/decision")
    public ResponseEntity<ApplicationResponse> decide(@PathVariable Long applicationId,
                                                        @RequestParam boolean accept,
                                                        Principal principal) {
        return ResponseEntity.ok(applicationService.decide(applicationId, principal.getName(), accept));
    }
}
