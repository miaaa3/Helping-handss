package com.example.HelpingHands.Repository;

import com.example.HelpingHands.Entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    void deleteByLikeId(Long likeId);

    /** Paginated notifications for the bell dropdown and the full notifications page. */
    Page<Notification> findByRecipientOrderByCreatedAtDesc(Long recipient, Pageable pageable);

    /** Unread count for the navbar badge. */
    long countByRecipientAndIsReadFalse(Long recipient);

    @Query("""
            SELECT COUNT(n) > 0
            FROM Notification n
            WHERE n.user.id = :actorId
              AND n.recipient = :recipient
              AND n.message = :message
              AND n.notificationType = :notificationType
            """)
    boolean existsSeedNotification(@Param("actorId") Long actorId,
                                   @Param("recipient") Long recipient,
                                   @Param("message") String message,
                                   @Param("notificationType") String notificationType);

    /** Bulk "mark all as read" for one user. */
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.recipient = :recipient AND n.isRead = false")
    void markAllAsReadForUser(@Param("recipient") Long recipient);
}
