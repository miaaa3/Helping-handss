package com.example.HelpingHands.DTO;

import com.example.HelpingHands.Entity.UserEntity;
import lombok.Data;

/** Public-facing profile data for any user, viewed by the currently authenticated user. */
@Data
public class PublicProfileDTO {
    private UserEntity user;
    private int numberOfFollowers;
    private int numberOfFollowing;
    /** Whether the requesting user has an ACCEPTED follow to this profile. */
    private boolean following;
    /** Whether this profile belongs to the requesting user. */
    private boolean ownProfile;
    /** "PENDING" if a follow request is awaiting acceptance, "ACCEPTED" if following, null otherwise. */
    private String followStatus;
}
