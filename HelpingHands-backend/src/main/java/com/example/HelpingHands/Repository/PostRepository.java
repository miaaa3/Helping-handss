package com.example.HelpingHands.Repository;

import com.example.HelpingHands.Entity.Comment;
import com.example.HelpingHands.Entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    /**
     * Posts from followed users, newest first, one page at a time.
     * Sorting is baked into the query (not driven by Pageable.getSort()) so it
     * stays compatible with the native SQL - pass an unsorted Pageable.
     */
    @Query(value = "SELECT p.* " +
            "FROM posts p " +
            "INNER JOIN follows f ON p.user_id = f.following_id " +
            "WHERE f.follower_id = :followerId " +
            "ORDER BY p.created_at DESC",
            countQuery = "SELECT COUNT(*) " +
            "FROM posts p " +
            "INNER JOIN follows f ON p.user_id = f.following_id " +
            "WHERE f.follower_id = :followerId",
            nativeQuery = true)
    Page<Post> findPostsOfFollowedUser(@Param("followerId") Long followerId, Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM likes WHERE post_id = :postId",nativeQuery = true)
    int numberOfLikes(@Param("postId") Long postId);

    @Query(value = "SELECT COUNT(*) from comments WHERE post_id= :postId",nativeQuery = true)
    int numberOfComments(@Param("postId") Long postId);

    /** All posts by one user, newest first - for profile pages, one page at a time. */
    Page<Post> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /** Total post count for one user - for profile/dashboard stats. */
    long countByUserId(Long userId);

    /** All posts platform-wide, newest first - for the admin moderation view. */
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
