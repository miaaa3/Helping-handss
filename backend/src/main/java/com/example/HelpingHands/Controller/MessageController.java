package com.example.HelpingHands.Controller;

import com.example.HelpingHands.DTO.ConversationSummary;
import com.example.HelpingHands.DTO.MessageRequest;
import com.example.HelpingHands.DTO.MessageResponse;
import com.example.HelpingHands.Repository.UserRepository;
import com.example.HelpingHands.Service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Conversation list for the sidebar: one entry per conversation partner,
     * with the last message preview and unread count.
     */
    @GetMapping("/conversations")
    public List<ConversationSummary> getConversations(Principal principal) {
        return messageService.getConversations(principal.getName());
    }

    /**
     * Full message history with a specific user. Opening this conversation
     * also marks the partner's pending messages as read.
     */
    @GetMapping("/conversation/{userId}")
    public List<MessageResponse> getConversation(@PathVariable Long userId, Principal principal) {
        return messageService.getConversation(principal.getName(), userId);
    }

    /**
     * Marks messages from {@code userId} as read without reloading the full history.
     * Called when a live message arrives while its conversation is already open.
     */
    @PutMapping("/conversation/{userId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long userId, Principal principal) {
        messageService.markAsRead(principal.getName(), userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * STOMP entry point for sending a chat message. Persists the message,
     * then pushes it to both the sender's and receiver's private
     * "/user/{email}/queue/messages" destinations for real-time delivery.
     */
    @MessageMapping("/chat.send")
    public void sendMessage(MessageRequest request, Principal principal) {
        MessageResponse response = messageService.sendMessage(principal.getName(), request);

        userRepository.findById(response.getReceiverId())
                .ifPresent(receiver -> messagingTemplate.convertAndSendToUser(
                        receiver.getEmail(), "/queue/messages", response));

        messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/messages", response);
    }
}
