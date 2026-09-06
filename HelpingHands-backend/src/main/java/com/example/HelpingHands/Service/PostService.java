package com.example.HelpingHands.Service;

import com.example.HelpingHands.Entity.MediaPost;
import com.example.HelpingHands.Entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface PostService {
    Post createPost(String content,Long userId);
    Post createPost(String content, Long userId,Set<MultipartFile> files) throws IOException;
    Post getPostById(Long id);
    List<Post> getAllPosts();
    List<MediaPost> getMediaForPost(Long postId);
    Post updatePost(Long id, Post updatedPost);
    void deletePost(Long id);
     void deleteMediaPostsByPostId(Long postId);

     /** One page of posts from followed users, newest first. */
     Page<Post> getPostsOfFollowedUsers(Long userId, Pageable pageable);

     /** One page of posts by one user, newest first - for profile pages. */
     Page<Post> getPostsByUserId(Long userId, Pageable pageable);
}
