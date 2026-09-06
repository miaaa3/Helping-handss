package com.example.HelpingHands.Entity;

/**
 * Lifecycle status of a volunteer's {@link OpportunityApplication}.
 */
public enum ApplicationStatus {
    /** Submitted, awaiting a decision from the organization. */
    PENDING,
    /** Organization accepted the volunteer. */
    ACCEPTED,
    /** Organization rejected the volunteer. */
    REJECTED,
    /** Volunteer withdrew the application. */
    CANCELLED
}
