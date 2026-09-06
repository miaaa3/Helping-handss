package com.example.HelpingHands.Service;

import com.example.HelpingHands.Entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface NotificationService {
    /** Marks one notification as read - only if it belongs to {@code userId}. */
    Notification markNotificationAsRead(Long notificationId, Long userId);
    void createCommentNotification(UserEntity user, Comment comment);
    void createLikeNotification(UserEntity user, Like like);
    void createFollowNotification(UserEntity follower, Follow follow);
    void createDonationNotification(UserEntity donor, Donation donation);
    /** Notifies the organization that a volunteer applied to one of its opportunities. */
    void createApplicationNotification(UserEntity volunteer, OpportunityApplication application);
    /** Notifies the volunteer that their application was accepted or rejected. */
    void createApplicationDecisionNotification(OpportunityApplication application);
    void deleteNotificationByLikeId(Long notificationId);
    Page<Notification> getNotificationsForUser(Long userId, Pageable pageable);
    long getUnreadCount(Long userId);
    void markAllAsRead(Long userId);
}
