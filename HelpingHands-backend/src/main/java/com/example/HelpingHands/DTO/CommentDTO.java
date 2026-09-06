package com.example.HelpingHands.DTO;

import com.example.HelpingHands.Entity.Comment;
import com.example.HelpingHands.Entity.UserEntity;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private String content;
    private LocalDateTime createdAt;
    private UserEntity user;
}
