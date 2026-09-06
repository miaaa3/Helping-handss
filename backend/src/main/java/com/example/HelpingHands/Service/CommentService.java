package com.example.HelpingHands.Service;

import com.example.HelpingHands.DTO.CommentDTO;
import com.example.HelpingHands.Entity.Comment;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CommentService {
    Comment createComment(Long postId, Long userId, String content);

    Set<CommentDTO> getCommentsByPostId(Long postId);

    List<Comment> getCommentsByUserId(Long userId);

    Comment updateComment(Long commentId, Comment updatedComment);

    void deleteComment(Long commentId);

}
