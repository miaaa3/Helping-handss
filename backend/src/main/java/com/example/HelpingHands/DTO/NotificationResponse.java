package com.example.HelpingHands.DTO;

import com.example.HelpingHands.Entity.Notification;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Lightweight notification view for the bell dropdown and notifications page -
 * exposes only the actor fields the UI needs, instead of the full UserEntity.
 */
@Data
@NoArgsConstructor
public class NotificationResponse {
    private Long id;
    private String message;
    private String notificationType;
    private LocalDateTime createdAt;
    private Boolean isRead;
    private String link;

    private Long actorId;
    private String actorName;
    private String actorProfile;

    public static NotificationResponse fromEntity(Notification notification) {
        NotificationResponse dto = new NotificationResponse();
        dto.setId(notification.getId());
        dto.setMessage(notification.getMessage());
        dto.setNotificationType(notification.getNotificationType());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setIsRead(Boolean.TRUE.equals(notification.getIsRead()));
        dto.setLink(notification.getLink());
        if (notification.getUser() != null) {
            dto.setActorId(notification.getUser().getId());
            dto.setActorName(notification.getUser().getName());
            dto.setActorProfile(notification.getUser().getProfile());
        }
        return dto;
    }
}
