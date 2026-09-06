package com.example.HelpingHands.Controller;

import com.example.HelpingHands.DTO.PostsWithLikeStatusDTO;
import com.example.HelpingHands.Entity.MediaPost;
import com.example.HelpingHands.Entity.Post;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Repository.PostRepository;
import com.example.HelpingHands.Service.CommentService;
import com.example.HelpingHands.Service.LikeService;
import com.example.HelpingHands.Service.PostService;
import com.example.HelpingHands.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@RequiredArgsConstructor
@RestController
@RequestMapping("api/posts")
public class PostController {
    private final PostService postService;
    private final CommentService commentService;
    private final PostRepository postRepository;
    private final UserService userService;
    private final LikeService likeService;
    @PostMapping("/createPost")
    public ResponseEntity<?> createPost(@RequestParam("content") String content,Principal principal,
                                        @RequestParam(value = "files", required = false) Set<MultipartFile> files) {
        UserEntity user = userService.findByEmail(principal.getName());
        try {
            if(files != null && !files.isEmpty()) {
                //files  provided along with content
                if (content.equals("undefined")) content="";
                Post createdPost = postService.createPost(content, user.getId(), files);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
            } else {
                //content only
                Post createdPost = postService.createPost(content, user.getId());
                return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/deletePost/{postId}")
    public void deleteOnePost(@PathVariable Long postId){
        postService.deletePost(postId);
    }


    /** Feed of posts from followed users, newest first, paginated (default 10/page). */
    @GetMapping("/getAllPosts")
    public ResponseEntity<?> getAllPosts(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          Principal principal) {
        UserEntity user = userService.findByEmail(principal.getName());
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postsPage = postService.getPostsOfFollowedUsers(user.getId(), pageable);
        List<PostsWithLikeStatusDTO> postsWithLikeStatusList = new ArrayList<>();
        for (Post post : postsPage.getContent()) {
            boolean isLiked = likeService.isPostLikedByUser(post.getId(), user.getId());
            PostsWithLikeStatusDTO postsDTO = new PostsWithLikeStatusDTO();
            postsDTO.setPost(post);
            postsDTO.setLiked(isLiked);
            postsDTO.setUser(post.getUser());
            postsDTO.setLikesNumber(postRepository.numberOfLikes(post.getId()));
            postsDTO.setCommentsNumber(postRepository.numberOfComments(post.getId()));
            postsDTO.setComments(commentService.getCommentsByPostId(post.getId()));
            postsWithLikeStatusList.add(postsDTO);
        }
        Page<PostsWithLikeStatusDTO> result = new PageImpl<>(postsWithLikeStatusList, pageable, postsPage.getTotalElements());
        return ResponseEntity.ok(result);
    }

    /** All posts by one user, newest first - for profile pages (own or someone else's), paginated (default 10/page). */
    @GetMapping("/getPostsByUser")
    public ResponseEntity<?> getPostsByUser(@RequestParam Long userId,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             Principal principal) {
        UserEntity currentUser = userService.findByEmail(principal.getName());
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postsPage = postService.getPostsByUserId(userId, pageable);
        List<PostsWithLikeStatusDTO> postsWithLikeStatusList = new ArrayList<>();
        for (Post post : postsPage.getContent()) {
            boolean isLiked = likeService.isPostLikedByUser(post.getId(), currentUser.getId());
            PostsWithLikeStatusDTO postsDTO = new PostsWithLikeStatusDTO();
            postsDTO.setPost(post);
            postsDTO.setLiked(isLiked);
            postsDTO.setUser(post.getUser());
            postsDTO.setLikesNumber(postRepository.numberOfLikes(post.getId()));
            postsDTO.setCommentsNumber(postRepository.numberOfComments(post.getId()));
            postsDTO.setComments(commentService.getCommentsByPostId(post.getId()));
            postsWithLikeStatusList.add(postsDTO);
        }
        Page<PostsWithLikeStatusDTO> result = new PageImpl<>(postsWithLikeStatusList, pageable, postsPage.getTotalElements());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getMediaForPost")
    public ResponseEntity<List<MediaPost>> getMediaForPost(@RequestParam Long postId) {
        List<MediaPost> mediaList = postService.getMediaForPost(postId);
        return ResponseEntity.ok(mediaList);
    }

    @GetMapping("/getPost")
    public ResponseEntity<Post> getPostById(@RequestParam Long id) {
        Post post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }
    @PutMapping("/updatePost")
    public ResponseEntity<Post> updatePost(@RequestParam Long id,@RequestBody Post updatedPost) {
        try {
            Post updated = postService.updatePost(id, updatedPost);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deletePost")
    public ResponseEntity<Void> deletePost(@RequestParam Long id) {
        try {
            postService.deleteMediaPostsByPostId(id);
            postService.deletePost(id);

            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

    }
    }
