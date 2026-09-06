package com.example.HelpingHands.ServiceImpl;

import com.example.HelpingHands.Configuration.StripeConfig;
import com.example.HelpingHands.DTO.CreateDonationRequest;
import com.example.HelpingHands.DTO.DonationResponse;
import com.example.HelpingHands.Entity.Donation;
import com.example.HelpingHands.Entity.DonationStatus;
import com.example.HelpingHands.Entity.Organization;
import com.example.HelpingHands.Entity.OrganizationVerificationStatus;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Repository.DonationRepository;
import com.example.HelpingHands.Repository.OrganizationRepository;
import com.example.HelpingHands.Repository.UserRepository;
import com.example.HelpingHands.Service.DonationService;
import com.example.HelpingHands.Service.NotificationService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class DonationServiceImpl implements DonationService {

    private final DonationRepository donationRepository;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final NotificationService notificationService;
    private final StripeConfig stripeConfig;

    @Override
    public Map<String, String> createDonationIntent(String donorEmail, CreateDonationRequest request) throws StripeException {
        UserEntity donor = userRepository.findByEmail(donorEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new IllegalArgumentException("Organization not found"));

        if (organization.getVerificationStatus() != OrganizationVerificationStatus.VERIFIED) {
            throw new IllegalStateException("This organization is not yet verified and cannot receive donations.");
        }

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ONE) < 0) {
            throw new IllegalStateException("Donation amount must be at least 1");
        }

        String currency = (request.getCurrency() == null || request.getCurrency().isBlank())
                ? "usd"
                : request.getCurrency().toLowerCase();

        long amountInSmallestUnit = request.getAmount()
                .multiply(BigDecimal.valueOf(100))
                .longValueExact();

        Donation donation = Donation.builder()
                .amount(request.getAmount())
                .currency(currency)
                .status(DonationStatus.PENDING)
                .message(request.getMessage())
                .anonymous(request.isAnonymous())
                .donor(donor)
                .organization(organization)
                .build();
        donation = donationRepository.save(donation);

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInSmallestUnit)
                .setCurrency(currency)
                .putMetadata("donationId", donation.getId().toString())
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build())
                .build();

        PaymentIntent intent = PaymentIntent.create(params);
        donation.setStripePaymentIntentId(intent.getId());
        donationRepository.save(donation);

        Map<String, String> result = new HashMap<>();
        result.put("clientSecret", intent.getClientSecret());
        result.put("donationId", donation.getId().toString());
        return result;
    }

    @Override
    public void handleStripeWebhook(String payload, String sigHeader) throws Exception {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, stripeConfig.getWebhookSecret());
        } catch (SignatureVerificationException e) {
            throw new IllegalArgumentException("Invalid Stripe webhook signature");
        }

        String paymentIntentId = switch (event.getType()) {
            case "payment_intent.succeeded", "payment_intent.payment_failed", "payment_intent.canceled" -> {
                PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
                yield intent != null ? intent.getId() : null;
            }
            case "charge.refunded" -> {
                Charge charge = (Charge) event.getDataObjectDeserializer().getObject().orElse(null);
                yield charge != null ? charge.getPaymentIntent() : null;
            }
            default -> null;
        };

        if (paymentIntentId == null) {
            if (!"payment_intent.succeeded".equals(event.getType())
                    && !"payment_intent.payment_failed".equals(event.getType())
                    && !"payment_intent.canceled".equals(event.getType())
                    && !"charge.refunded".equals(event.getType())) {
                return; // event type we don't care about
            }
            log.warn("Stripe webhook {} had no deserializable payment intent", event.getId());
            return;
        }

        Donation donation = donationRepository.findByStripePaymentIntentId(paymentIntentId).orElse(null);
        if (donation == null) {
            log.warn("Received Stripe webhook for unknown payment intent {}", paymentIntentId);
            return;
        }

        switch (event.getType()) {
            case "payment_intent.succeeded" -> {
                donation.setStatus(DonationStatus.SUCCEEDED);
                donationRepository.save(donation);
                notificationService.createDonationNotification(donation.getDonor(), donation);
            }
            case "payment_intent.payment_failed", "payment_intent.canceled" -> {
                donation.setStatus(DonationStatus.FAILED);
                donationRepository.save(donation);
            }
            case "charge.refunded" -> {
                donation.setStatus(DonationStatus.REFUNDED);
                donationRepository.save(donation);
            }
            default -> { /* unreachable */ }
        }
    }

    @Override
    public List<DonationResponse> getOrganizationDonations(Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new IllegalArgumentException("Organization not found"));

        return donationRepository.findByOrganizationAndStatusOrderByCreatedAtDesc(organization, DonationStatus.SUCCEEDED)
                .stream()
                .map(DonationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<DonationResponse> getMyDonations(String donorEmail) {
        UserEntity donor = userRepository.findByEmail(donorEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return donationRepository.findByDonorOrderByCreatedAtDesc(donor)
                .stream()
                .map(DonationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getTotalRaised(Long organizationId) {
        return donationRepository.getTotalRaisedForOrganization(organizationId);
    }
}
