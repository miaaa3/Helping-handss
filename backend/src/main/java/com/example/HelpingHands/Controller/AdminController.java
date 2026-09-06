package com.example.HelpingHands.Controller;

import com.example.HelpingHands.DTO.AdminOrganizationResponse;
import com.example.HelpingHands.DTO.AdminPostResponse;
import com.example.HelpingHands.DTO.AdminUserResponse;
import com.example.HelpingHands.DTO.AdminUserStatusRequest;
import com.example.HelpingHands.DTO.AdminVerificationRequest;
import com.example.HelpingHands.DTO.DonationResponse;
import com.example.HelpingHands.DTO.OpportunityResponse;
import com.example.HelpingHands.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Admin-only endpoints: organization verification, user management, content moderation, donation activity. */
@RequiredArgsConstructor
@RestController
@RequestMapping("api/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/organizations")
    public ResponseEntity<List<AdminOrganizationResponse>> listOrganizations() {
        return ResponseEntity.ok(adminService.listOrganizations());
    }

    @PatchMapping("/organizations/{id}/verification")
    public ResponseEntity<AdminOrganizationResponse> setVerification(@PathVariable Long id,
                                                                       @RequestBody AdminVerificationRequest request) {
        return ResponseEntity.ok(adminService.setOrganizationVerification(id, request.getStatus()));
    }

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserResponse>> listUsers() {
        return ResponseEntity.ok(adminService.listUsers());
    }

    @PatchMapping("/users/{id}/status")
    public ResponseEntity<AdminUserResponse> setUserStatus(@PathVariable Long id,
                                                            @RequestBody AdminUserStatusRequest request) {
        return ResponseEntity.ok(adminService.setUserEnabled(id, request.isEnabled()));
    }

    @GetMapping("/posts")
    public ResponseEntity<Page<AdminPostResponse>> listPosts(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(adminService.listPosts(pageable));
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        adminService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/opportunities")
    public ResponseEntity<List<OpportunityResponse>> listOpportunities() {
        return ResponseEntity.ok(adminService.listOpportunities());
    }

    @DeleteMapping("/opportunities/{id}")
    public ResponseEntity<Void> deleteOpportunity(@PathVariable Long id) {
        adminService.deleteOpportunity(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/donations")
    public ResponseEntity<Page<DonationResponse>> listDonations(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(adminService.listDonations(pageable));
    }
}
