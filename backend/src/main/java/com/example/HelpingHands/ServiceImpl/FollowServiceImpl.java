package com.example.HelpingHands.ServiceImpl;

import com.example.HelpingHands.Entity.Follow;
import com.example.HelpingHands.Entity.FollowStatus;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Repository.FollowRepository;
import com.example.HelpingHands.Repository.UserRepository;
import com.example.HelpingHands.Service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Override
    public Follow createFollow(UserEntity follower, UserEntity following) {
        // new Follow constructor already sets status = PENDING
        Follow follow = new Follow(follower, following);
        return followRepository.save(follow);
    }

    /** True only when an ACCEPTED follow relationship exists. */
    @Override
    public boolean isFollowing(UserEntity follower, UserEntity following) {
        return followRepository.existsByFollowerAndFollowingAndStatus(follower, following, FollowStatus.ACCEPTED);
    }

    /** Check if a follow request (PENDING or ACCEPTED) already exists. */
    public boolean hasAnyFollow(UserEntity follower, UserEntity following) {
        return followRepository.existsByFollowerAndFollowing(follower, following);
    }

    @Override
    public List<UserEntity> getFollowers(UserEntity user) {
        // Only accepted relationships count as real followers.
        return followRepository.findByFollowingAndStatus(user, FollowStatus.ACCEPTED)
                .stream().map(Follow::getFollower).collect(Collectors.toList());
    }

    @Override
    public List<UserEntity> getFollowing(Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user == null) return List.of();
        // Only accepted follows; also drives the followed-orgs feed.
        return followRepository.findByFollowerAndStatus(user, FollowStatus.ACCEPTED)
                .stream().map(Follow::getFollowing).collect(Collectors.toList());
    }

    @Override
    public void unfollow(UserEntity follower, UserEntity following) {
        followRepository.deleteByFollowerAndFollowing(follower, following);
    }

    @Override
    public void deleteFollow(Follow follow) {
        followRepository.delete(follow);
    }

    @Override
    public Follow findFollowByFollowerAndFollowing(UserEntity follower, UserEntity following) {
        return followRepository.findByFollowerAndFollowing(follower, following);
    }

    @Override
    public List<Follow> getPendingRequests(UserEntity user) {
        return followRepository.findByFollowingAndStatus(user, FollowStatus.PENDING);
    }

    @Override
    public Follow acceptRequest(Long followId, UserEntity currentUser) {
        return followRepository.findById(followId).map(follow -> {
            // Security: only the target user can accept their own requests
            if (!follow.getFollowing().getId().equals(currentUser.getId())) return null;
            follow.setStatus(FollowStatus.ACCEPTED);
            return followRepository.save(follow);
        }).orElse(null);
    }

    @Override
    public void rejectRequest(Long followId, UserEntity currentUser) {
        followRepository.findById(followId).ifPresent(follow -> {
            if (follow.getFollowing().getId().equals(currentUser.getId())) {
                followRepository.delete(follow);
            }
        });
    }
}
