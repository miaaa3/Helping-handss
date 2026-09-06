package com.example.HelpingHands.Controller;

import com.example.HelpingHands.DTO.CommentDTO;
import com.example.HelpingHands.Entity.Comment;
import com.example.HelpingHands.Entity.Notification;
import com.example.HelpingHands.Entity.Post;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Repository.PostRepository;
import com.example.HelpingHands.Service.CommentService;
import com.example.HelpingHands.Service.NotificationService;
import com.example.HelpingHands.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;
    private final PostRepository postRepository;
    private final UserService userService;
    private final NotificationService notificationService;

    @PostMapping("/createComment")
    public ResponseEntity<Comment> createComment(@RequestParam("postId") Long postId, @RequestParam("content") String content,
                                                 Principal principal) {
        UserEntity user = userService.findByEmail(principal.getName());
        Comment createdComment = commentService.createComment(postId, user.getId(), content);
        notificationService.createCommentNotification(user,createdComment);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @GetMapping("/getCommentsByPostId")
    public ResponseEntity<Set<CommentDTO>> getCommentsByPostId(@RequestParam Long postId) {
            return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }
    @PutMapping("/updateComment")
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentId,@RequestBody Comment updatedComment) {
        try {
            Comment updated = commentService.updateComment(commentId, updatedComment);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteComment")
    public ResponseEntity<Void> deleteComment(@RequestParam Long commentId) {
        try {
            commentService.deleteComment(commentId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
