package com.example.HelpingHands.ServiceImpl;

import com.example.HelpingHands.Entity.*;
import com.example.HelpingHands.Repository.NotificationRepository;
import com.example.HelpingHands.Service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Override
    public Notification markNotificationAsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));

        if (!notification.getRecipient().equals(userId)) {
            throw new EntityNotFoundException("Notification not found");
        }

        notification.setIsRead(true);
        return notificationRepository.save(notification);
    }

    @Override
    public void createLikeNotification(UserEntity user, Like like) {
        if (!user.getId().equals(like.getPost().getUser().getId())) {
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setMessage(" liked your post.");
            notification.setNotificationType("LIKE");
            notification.setLike(like);
            notification.setRecipient(like.getPost().getUser().getId());
            notification.setLink("/user-profile");
            notificationRepository.save(notification);
        }
    }

    @Override
    public void createFollowNotification(UserEntity follower, Follow follow) {
        Notification notification = new Notification();
        notification.setUser(follower);
        notification.setMessage(" started following you.");
        notification.setNotificationType("FOLLOW");
        notification.setFollow(follow);
        notification.setRecipient(follow.getFollowing().getId());
        notification.setLink("/user-profile/" + follower.getId());
        notificationRepository.save(notification);
    }

    @Override
    public void createDonationNotification(UserEntity donor, Donation donation) {
        Notification notification = new Notification();
        notification.setUser(donor);
        notification.setMessage(" donated to your organization.");
        notification.setNotificationType("DONATION");
        notification.setRecipient(donation.getOrganization().getId());
        notification.setLink("/dashboard");
        notificationRepository.save(notification);
    }

    @Override
    public void createApplicationNotification(UserEntity volunteer, OpportunityApplication application) {
        Notification notification = new Notification();
        notification.setUser(volunteer);
        notification.setMessage(" applied to your opportunity.");
        notification.setNotificationType("APPLICATION");
        notification.setRecipient(application.getOpportunity().getOrganization().getId());
        notification.setLink("/dashboard");
        notificationRepository.save(notification);
    }

    @Override
    public void createApplicationDecisionNotification(OpportunityApplication application) {
        Notification notification = new Notification();
        notification.setUser(application.getOpportunity().getOrganization());
        boolean accepted = application.getStatus() == ApplicationStatus.ACCEPTED;
        notification.setMessage(accepted ? " accepted your application." : " rejected your application.");
        notification.setNotificationType("APPLICATION_DECISION");
        notification.setRecipient(application.getVolunteer().getId());
        notification.setLink("/dashboard");
        notificationRepository.save(notification);
    }

    @Override
    public void createCommentNotification(UserEntity user, Comment comment) {
        if (!user.getId().equals(comment.getPost().getUser().getId())) {
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setMessage(" commented on your post.");
            notification.setNotificationType("COMMENT");
            notification.setComment(comment);
            notification.setRecipient(comment.getPost().getUser().getId());
            notification.setLink("/user-profile");
            notificationRepository.save(notification);
        }
    }

    @Override
    public void deleteNotificationByLikeId(Long likeId) {
        notificationRepository.deleteByLikeId(likeId);
    }

    @Override
    public Page<Notification> getNotificationsForUser(Long userId, Pageable pageable) {
        return notificationRepository.findByRecipientOrderByCreatedAtDesc(userId, pageable);
    }

    @Override
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByRecipientAndIsReadFalse(userId);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsReadForUser(userId);
    }
}
