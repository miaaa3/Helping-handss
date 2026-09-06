package com.example.HelpingHands.DTO;

import com.example.HelpingHands.Entity.Donation;
import com.example.HelpingHands.Entity.DonationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class DonationResponse {
    private Long id;
    private BigDecimal amount;
    private String currency;
    private DonationStatus status;
    private String message;
    private boolean anonymous;
    private LocalDateTime createdAt;
    private Long donorId;
    private String donorName;
    private Long organizationId;
    private String organizationName;
    private String transactionRef;

    public static DonationResponse fromEntity(Donation donation) {
        DonationResponse dto = new DonationResponse();
        dto.setId(donation.getId());
        dto.setAmount(donation.getAmount());
        dto.setCurrency(donation.getCurrency());
        dto.setStatus(donation.getStatus());
        dto.setMessage(donation.getMessage());
        dto.setAnonymous(donation.getAnonymous());
        dto.setCreatedAt(donation.getCreatedAt());
        dto.setOrganizationId(donation.getOrganization().getId());
        dto.setOrganizationName(donation.getOrganization().getName());
        dto.setTransactionRef(donation.getStripePaymentIntentId());

        if (donation.getAnonymous()) {
            dto.setDonorId(null);
            dto.setDonorName("Anonymous");
        } else {
            dto.setDonorId(donation.getDonor().getId());
            dto.setDonorName(donation.getDonor().getName());
        }
        return dto;
    }
}
