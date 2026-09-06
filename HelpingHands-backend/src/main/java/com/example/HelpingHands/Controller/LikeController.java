package com.example.HelpingHands.Controller;

import com.example.HelpingHands.Entity.Like;
import com.example.HelpingHands.Entity.Notification;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Service.LikeService;
import com.example.HelpingHands.Service.NotificationService;
import com.example.HelpingHands.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {
    private final LikeService likeService;
    private final UserService userService;
    private final NotificationService notificationService;
    @PostMapping("/createLike")
    public ResponseEntity<Like> createLike(@RequestParam Long postId,Principal principal) {

        UserEntity user = userService.findByEmail(principal.getName());
        try {

            Like like = likeService.createLike(postId, user.getId());

            if (like != null) {
                notificationService.createLikeNotification(user,like);
                return ResponseEntity.status(HttpStatus.CREATED).body(like);
            } else {
                notificationService.deleteNotificationByLikeId(like.getId());
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @DeleteMapping("/deleteLike")
    public ResponseEntity<Void> deleteLike(@RequestParam Long likeId) {
        try {
            likeService.deleteLike(likeId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

