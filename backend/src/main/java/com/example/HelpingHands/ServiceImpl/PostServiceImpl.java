package com.example.HelpingHands.ServiceImpl;

import com.example.HelpingHands.Entity.MediaPost;
import com.example.HelpingHands.Entity.Post;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Repository.MediaPostRepository;
import com.example.HelpingHands.Repository.PostRepository;
import com.example.HelpingHands.Repository.UserRepository;
import com.example.HelpingHands.Service.PostService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final MediaPostRepository mediaPostRepository;

    @Value("${upload.directory}")
    private String uploadDirectory;

    @PostConstruct
    private void initUploadDirectory() throws IOException {
        Path path = Paths.get(uploadDirectory);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
            log.info("Created upload directory at {}", path.toAbsolutePath());
        }
    }

    @Override
    public Post createPost(String content, Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));
        Post post = new Post(content, user);
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public Post createPost(String content, Long userId, Set<MultipartFile> files) throws IOException {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));
        Post post = new Post(content, user);
        Set<MediaPost> mediaList = new HashSet<>();

        for (MultipartFile file : files) {
            String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String fileExtension = getFileExtension(originalFileName);
            String uniqueFileName = generateUniqueFileName(fileExtension);

            Path targetLocation = Paths.get(uploadDirectory, uniqueFileName);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            MediaPost media = new MediaPost();
            media.setFileName(uniqueFileName);
            media.setFileType(file.getContentType());

            // Set the file path as a string
            media.setFile(targetLocation.toString());

            // Set the relationship to the Post entity
            media.setPost(post);
            mediaPostRepository.save(media);
            mediaList.add(media);
        }

        post.setMedia(mediaList);
        return postRepository.save(post);
    }

    private String generateUniqueFileName(String fileExtension) {
        String uuid = UUID.randomUUID().toString();
        return uuid + fileExtension;
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1) {
            return fileName.substring(dotIndex);
        }
        return ""; //no file extension
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }


    @Override
    public List<MediaPost> getMediaForPost(Long postId) {
        return mediaPostRepository.findByPostId(postId);
    }

    @Override
    public Post updatePost(Long id, Post updatedPost) {
        Optional<Post> existingPost = postRepository.findById(id);
        if (existingPost.isPresent()) {
            updatedPost.setId(id);
            return postRepository.save(updatedPost);
        } else {
            throw new IllegalArgumentException("Post with id " + id + " not found");
        }
    }


    @Override
    public void deletePost(Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Post with id " + id + " not found");
        }
    }

    @Override
    public void deleteMediaPostsByPostId(Long postId) {
        List<MediaPost> mediaPosts = mediaPostRepository.findByPostId(postId);
        mediaPostRepository.deleteAll(mediaPosts);
    }

    @Override
    public Page<Post> getPostsOfFollowedUsers(Long userId, Pageable pageable) {
        if (userRepository.existsById(userId)) {
            return postRepository.findPostsOfFollowedUser(userId, pageable);
        }
        return Page.empty(pageable);
    }

    @Override
    public Page<Post> getPostsByUserId(Long userId, Pageable pageable) {
        return postRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

}
