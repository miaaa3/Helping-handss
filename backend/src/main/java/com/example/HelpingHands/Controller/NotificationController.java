package com.example.HelpingHands.Controller;

import com.example.HelpingHands.DTO.NotificationResponse;
import com.example.HelpingHands.Entity.Notification;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Service.NotificationService;
import com.example.HelpingHands.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    private final UserService userService;

    /** Paginated notifications for the current user, newest first. */
    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> getNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Principal principal) {
        UserEntity user = userService.findByEmail(principal.getName());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Notification> notifications = notificationService.getNotificationsForUser(user.getId(), pageable);
        return ResponseEntity.ok(notifications.map(NotificationResponse::fromEntity));
    }

    /** Unread notification count, for the navbar badge. */
    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(Principal principal) {
        UserEntity user = userService.findByEmail(principal.getName());
        return ResponseEntity.ok(Map.of("count", notificationService.getUnreadCount(user.getId())));
    }

    /** Marks one notification as read. */
    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable Long id, Principal principal) {
        UserEntity user = userService.findByEmail(principal.getName());
        Notification notification = notificationService.markNotificationAsRead(id, user.getId());
        return ResponseEntity.ok(NotificationResponse.fromEntity(notification));
    }

    /** Marks all of the current user's notifications as read. */
    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(Principal principal) {
        UserEntity user = userService.findByEmail(principal.getName());
        notificationService.markAllAsRead(user.getId());
        return ResponseEntity.noContent().build();
    }
}
