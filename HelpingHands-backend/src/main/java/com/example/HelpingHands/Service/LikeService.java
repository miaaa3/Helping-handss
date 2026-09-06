package com.example.HelpingHands.Service;

import com.example.HelpingHands.Entity.Like;

import java.util.List;

public interface LikeService {
    Like createLike(Long postId, Long userId);
    boolean isPostLikedByUser(Long postId, Long userId);
    Long getLikesNumberByPost(Long postId);
    void deleteLike(Long likeId);
}
