package com.example.HelpingHands.DTO;

import com.example.HelpingHands.Entity.Follow;
import lombok.Data;

@Data
public class FollowDTO {
    private Follow follow;
    private boolean isFollowed;
    /** "PENDING" when a request has been sent but not yet accepted; null when unfollowed. */
    private String status;
}
