package com.example.HelpingHands.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * Flat view of a pending follow request for the client.
 * Needed because Follow.follower is @JsonBackReference (not serialized),
 * so the raw entity never tells the client who the request is from.
 */
@Data
@AllArgsConstructor
public class PendingRequestDTO {
    private Long id;
    private Date followedAt;
    private String status;
    private Follower follower;

    @Data
    @AllArgsConstructor
    public static class Follower {
        private Long id;
        private String name;
        private String profile;
    }
}
