package com.example.HelpingHands.Repository;

import com.example.HelpingHands.Entity.Like;
import com.example.HelpingHands.Entity.Post;
import com.example.HelpingHands.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like,Long> {

    List<Like> findByUserId(Long userId);

    List<Like> findByPostId(Long postId);

    @Query(value = "SELECT * FROM Likes WHERE post_id = :post AND user_id = :user", nativeQuery = true)
    Like findByPostAndUser(@Param("post") Long postId, @Param("user") Long userId);

    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);

    Long countByPostId(Long postId);
}
