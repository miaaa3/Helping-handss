package com.example.HelpingHands.DTO;

import com.example.HelpingHands.Entity.Post;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/** Row shape for the admin "content moderation" posts table. */
@Data
@NoArgsConstructor
public class AdminPostResponse {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long authorId;
    private String authorName;
    private String authorProfile;
    private int mediaCount;

    public static AdminPostResponse fromEntity(Post post) {
        AdminPostResponse dto = new AdminPostResponse();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setMediaCount(post.getMedia() != null ? post.getMedia().size() : 0);
        if (post.getUser() != null) {
            dto.setAuthorId(post.getUser().getId());
            dto.setAuthorName(post.getUser().getName());
            dto.setAuthorProfile(post.getUser().getProfile());
        }
        return dto;
    }
}
