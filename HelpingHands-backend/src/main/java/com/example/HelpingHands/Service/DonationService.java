package com.example.HelpingHands.Service;

import com.example.HelpingHands.DTO.CreateDonationRequest;
import com.example.HelpingHands.DTO.DonationResponse;
import com.stripe.exception.StripeException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface DonationService {
    Map<String, String> createDonationIntent(String donorEmail, CreateDonationRequest request) throws StripeException;

    void handleStripeWebhook(String payload, String sigHeader) throws Exception;

    List<DonationResponse> getOrganizationDonations(Long organizationId);

    List<DonationResponse> getMyDonations(String donorEmail);

    BigDecimal getTotalRaised(Long organizationId);
}
