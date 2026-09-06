package com.example.HelpingHands.Controller;

import com.example.HelpingHands.Configuration.StripeConfig;
import com.example.HelpingHands.DTO.CreateDonationRequest;
import com.example.HelpingHands.DTO.DonationResponse;
import com.example.HelpingHands.Service.DonationService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/donations")
public class DonationController {

    private final DonationService donationService;
    private final StripeConfig stripeConfig;

    @PostMapping("/create-intent")
    public ResponseEntity<Map<String, String>> createIntent(@RequestBody CreateDonationRequest request, Principal principal) throws StripeException {
        Map<String, String> result = donationService.createDonationIntent(principal.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestBody String payload,
                                               @RequestHeader("Stripe-Signature") String signature) throws Exception {
        donationService.handleStripeWebhook(payload, signature);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<DonationResponse>> getOrganizationDonations(@PathVariable Long organizationId) {
        return ResponseEntity.ok(donationService.getOrganizationDonations(organizationId));
    }

    @GetMapping("/organization/{organizationId}/total")
    public ResponseEntity<BigDecimal> getTotalRaised(@PathVariable Long organizationId) {
        return ResponseEntity.ok(donationService.getTotalRaised(organizationId));
    }

    @GetMapping("/my-donations")
    public ResponseEntity<List<DonationResponse>> getMyDonations(Principal principal) {
        return ResponseEntity.ok(donationService.getMyDonations(principal.getName()));
    }

    @GetMapping("/config")
    public ResponseEntity<Map<String, String>> getConfig() {
        return ResponseEntity.ok(Map.of("publishableKey", stripeConfig.getPublishableKey()));
    }
}
