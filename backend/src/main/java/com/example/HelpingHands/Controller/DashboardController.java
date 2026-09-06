package com.example.HelpingHands.Controller;

import com.example.HelpingHands.DTO.OrganizationDashboardResponse;
import com.example.HelpingHands.DTO.VolunteerDashboardResponse;
import com.example.HelpingHands.Service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    /** Summary for the current volunteer's dashboard. */
    @GetMapping("/volunteer")
    public ResponseEntity<VolunteerDashboardResponse> getVolunteerDashboard(Principal principal) {
        return ResponseEntity.ok(dashboardService.getVolunteerDashboard(principal.getName()));
    }

    /** Summary for the current organization's dashboard. */
    @GetMapping("/organization")
    public ResponseEntity<OrganizationDashboardResponse> getOrganizationDashboard(Principal principal) {
        return ResponseEntity.ok(dashboardService.getOrganizationDashboard(principal.getName()));
    }
}
