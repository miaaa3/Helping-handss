package com.example.HelpingHands.ServiceImpl;

import com.example.HelpingHands.DTO.CommentDTO;
import com.example.HelpingHands.Entity.Comment;
import com.example.HelpingHands.Entity.Post;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Exception.CommentNotFoundException;
import com.example.HelpingHands.Repository.CommentRepository;
import com.example.HelpingHands.Repository.PostRepository;
import com.example.HelpingHands.Repository.UserRepository;
import com.example.HelpingHands.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public Comment createComment(Long postId, Long userId, String content) {
        Optional<Post> postOptional = postRepository.findById(postId);
        Optional<UserEntity> userOptional = userRepository.findById(userId);

        if (postOptional.isPresent() && userOptional.isPresent()) {
            Comment comment = new Comment();
            comment.setPost(postOptional.get());
            comment.setUser(userOptional.get());
            comment.setContent(content);
            return commentRepository.save(comment);
        } else {
            throw new IllegalArgumentException("Post or User not found for given IDs");
        }
    }

    @Override
    public Set<CommentDTO> getCommentsByPostId(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        Set<Comment> comments = post.get().getComments();
        List<Comment> sortedComments = comments.stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt).reversed())
                .collect(Collectors.toList());

        Set<CommentDTO> commentSet = new LinkedHashSet<>();

        for (Comment comment : comments) {
            CommentDTO commentDto = new CommentDTO();
            commentDto.setContent(comment.getContent());
            commentDto.setCreatedAt(comment.getCreatedAt());
            commentDto.setUser(comment.getUser());
            commentSet.add(commentDto);
        }
        return commentSet;
    }

    @Override
    public List<Comment> getCommentsByUserId(Long userId) {
        return commentRepository.findByUserId(userId);
    }

    @Override
    public Comment updateComment(Long commentId, Comment updatedComment) {
        Optional<Comment> existingComment = commentRepository.findById(commentId);
        if (existingComment.isPresent()) {
            updatedComment.setId(commentId);
            return commentRepository.save(updatedComment);
        } else {
            throw new CommentNotFoundException("Comment with id " + commentId + " not found");
        }
    }

    @Override
    public void deleteComment(Long commentId) {
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
        } else {
            throw new CommentNotFoundException("Comment with id " + commentId + " not found");
        }
    }
}
