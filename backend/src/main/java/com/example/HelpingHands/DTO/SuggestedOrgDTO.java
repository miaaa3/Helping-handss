package com.example.HelpingHands.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

/** Lightweight organization card for the "Organizations you might like" section. */
@Data
@AllArgsConstructor
public class SuggestedOrgDTO {
    private Long id;
    private String name;
    private String profile;
    private String type;
    private String description;
}
