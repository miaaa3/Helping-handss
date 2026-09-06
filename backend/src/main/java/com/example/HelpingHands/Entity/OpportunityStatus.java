package com.example.HelpingHands.Entity;

public enum OpportunityStatus {
    /** Created but not yet visible to volunteers. */
    DRAFT,
    /** Visible and accepting applications. */
    OPEN,
    /** Needed volunteer count reached; no longer accepting applications. */
    FULL,
    /** Opportunity has ended or was cancelled. */
    CLOSED
}
