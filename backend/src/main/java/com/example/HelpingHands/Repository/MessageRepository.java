package com.example.HelpingHands.Repository;

import com.example.HelpingHands.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m " +
            "WHERE (m.sender.id = :userId AND m.receiver.id = :otherUserId) " +
            "   OR (m.sender.id = :otherUserId AND m.receiver.id = :userId) " +
            "ORDER BY m.createdAt ASC")
    List<Message> findConversation(@Param("userId") Long userId, @Param("otherUserId") Long otherUserId);

    @Query("SELECT DISTINCT CASE WHEN m.sender.id = :userId THEN m.receiver.id ELSE m.sender.id END " +
            "FROM Message m WHERE m.sender.id = :userId OR m.receiver.id = :userId")
    List<Long> findConversationPartnerIds(@Param("userId") Long userId);

    long countBySenderIdAndReceiverIdAndIsReadFalse(Long senderId, Long receiverId);

    List<Message> findBySenderIdAndReceiverIdAndIsReadFalse(Long senderId, Long receiverId);

    /** Total unread messages across all conversations - for the volunteer/org dashboard. */
    long countByReceiverIdAndIsReadFalse(Long receiverId);
}
