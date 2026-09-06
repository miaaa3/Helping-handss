package com.example.HelpingHands.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationSummary {
    private Long otherUserId;
    private String otherUserName;
    private String otherUserProfile;
    private String lastMessage;
    private LocalDateTime lastMessageAt;
    private long unreadCount;
}
