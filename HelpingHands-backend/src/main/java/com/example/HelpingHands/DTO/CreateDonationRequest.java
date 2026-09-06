package com.example.HelpingHands.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateDonationRequest {
    private Long organizationId;
    private BigDecimal amount;
    private String currency = "usd";
    private String message;
    private boolean anonymous;
}
