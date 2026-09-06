package com.example.HelpingHands.ServiceImpl;

import com.example.HelpingHands.Entity.Like;
import com.example.HelpingHands.Entity.Post;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Repository.LikeRepository;
import com.example.HelpingHands.Repository.PostRepository;
import com.example.HelpingHands.Repository.UserRepository;
import com.example.HelpingHands.Service.LikeService;
import com.example.HelpingHands.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class LikeServiceImpl implements LikeService {
    private final   LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final   UserRepository userRepository;


    @Override
    public Like createLike(Long postId, Long userId) {
        Optional<Post> post = postRepository.findById(postId);
        Optional<UserEntity> user = userRepository.findById(userId);

        if (post.isPresent() && user.isPresent()) {
            Like existingLike = likeRepository.findByPostAndUser(postId, userId);
            if (existingLike != null) {
                likeRepository.delete(existingLike);
                return null;
            } else {
                Like like = new Like();
                like.setPost(post.get());
                like.setUser(user.get());
                return likeRepository.save(like);
            }
        } else {
            throw new IllegalArgumentException("Post or User not found for given IDs");
        }
    }

    @Override
    public boolean isPostLikedByUser(Long postId, Long userId) {
        return likeRepository.findByPostAndUser(postId, userId) != null;
    }


    @Override
    public Long getLikesNumberByPost(Long postId) {
        return likeRepository.countByPostId(postId);
    }

    @Override
    public void deleteLike(Long likeId) {
        if (likeRepository.existsById(likeId)) {
            likeRepository.deleteById(likeId);
        } else {
            throw new IllegalArgumentException("Like with id " + likeId + " not found");
        }
    }
}
