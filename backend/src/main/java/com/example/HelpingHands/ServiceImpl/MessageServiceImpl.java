package com.example.HelpingHands.ServiceImpl;

import com.example.HelpingHands.DTO.ConversationSummary;
import com.example.HelpingHands.DTO.MessageRequest;
import com.example.HelpingHands.DTO.MessageResponse;
import com.example.HelpingHands.Entity.Message;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Repository.MessageRepository;
import com.example.HelpingHands.Repository.UserRepository;
import com.example.HelpingHands.Service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public MessageResponse sendMessage(String senderEmail, MessageRequest request) {
        UserEntity sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new IllegalStateException("Message content cannot be empty");
        }

        UserEntity receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new IllegalArgumentException("Recipient not found"));

        if (receiver.getId().equals(sender.getId())) {
            throw new IllegalStateException("Cannot send a message to yourself");
        }

        Message message = Message.builder()
                .content(request.getContent())
                .isRead(false)
                .sender(sender)
                .receiver(receiver)
                .build();

        message = messageRepository.save(message);
        return MessageResponse.fromEntity(message);
    }

    @Override
    @Transactional
    public List<MessageResponse> getConversation(String userEmail, Long otherUserId) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        userRepository.findById(otherUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Message> messages = messageRepository.findConversation(user.getId(), otherUserId);

        // Mark incoming messages as read now that the user has opened the conversation
        markIncomingAsRead(otherUserId, user.getId());

        return messages.stream()
                .map(MessageResponse::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public void markAsRead(String userEmail, Long otherUserId) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        markIncomingAsRead(otherUserId, user.getId());
    }

    /** Flips isRead to true for all unread messages sent by {@code senderId} to {@code receiverId}. */
    private void markIncomingAsRead(Long senderId, Long receiverId) {
        messageRepository.findBySenderIdAndReceiverIdAndIsReadFalse(senderId, receiverId)
                .forEach(m -> m.setIsRead(true));
    }

    @Override
    @Transactional
    public List<ConversationSummary> getConversations(String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Long> partnerIds = messageRepository.findConversationPartnerIds(user.getId());
        List<ConversationSummary> summaries = new ArrayList<>();

        for (Long partnerId : partnerIds) {
            UserEntity partner = userRepository.findById(partnerId).orElse(null);
            if (partner == null) {
                continue;
            }

            List<Message> conversation = messageRepository.findConversation(user.getId(), partnerId);
            if (conversation.isEmpty()) {
                continue;
            }

            Message lastMessage = conversation.get(conversation.size() - 1);
            long unreadCount = messageRepository.countBySenderIdAndReceiverIdAndIsReadFalse(partnerId, user.getId());

            summaries.add(new ConversationSummary(
                    partner.getId(),
                    partner.getName(),
                    partner.getProfile(),
                    lastMessage.getContent(),
                    lastMessage.getCreatedAt(),
                    unreadCount
            ));
        }

        summaries.sort((a, b) -> b.getLastMessageAt().compareTo(a.getLastMessageAt()));
        return summaries;
    }
}
