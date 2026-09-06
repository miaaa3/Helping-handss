package com.example.HelpingHands.Repository;

import com.example.HelpingHands.Entity.MediaPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MediaPostRepository extends JpaRepository<MediaPost,Long> {
    List<MediaPost> findByPostId(Long postId);
}
