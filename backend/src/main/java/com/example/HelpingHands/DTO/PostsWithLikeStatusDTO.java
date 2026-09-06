package com.example.HelpingHands.DTO;

import com.example.HelpingHands.Entity.Comment;
import com.example.HelpingHands.Entity.Post;
import com.example.HelpingHands.Entity.UserEntity;
import lombok.Data;

import java.util.Set;


@Data
public class PostsWithLikeStatusDTO {
    private Post post;
    private boolean isLiked;
    private UserEntity user;
    private int likesNumber;
    private int commentsNumber;
    private Set<CommentDTO> comments;
}
