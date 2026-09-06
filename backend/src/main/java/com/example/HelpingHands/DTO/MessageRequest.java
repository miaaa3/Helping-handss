package com.example.HelpingHands.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageRequest {
    private Long receiverId;
    private String content;
}
