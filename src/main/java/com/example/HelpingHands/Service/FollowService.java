package com.example.HelpingHands.Service;

import com.example.HelpingHands.Entity.Follow;
import com.example.HelpingHands.Entity.UserEntity;

import java.util.List;

public interface FollowService {

    Follow createFollow(UserEntity follower, UserEntity following);
    /** True only when an ACCEPTED follow relationship exists. */
    boolean isFollowing(UserEntity follower, UserEntity following);
    List<UserEntity> getFollowers(UserEntity user);
    List<UserEntity> getFollowing(Long userId);
    void unfollow(UserEntity follower, UserEntity following);
    void deleteFollow(Follow follow);
    Follow findFollowByFollowerAndFollowing(UserEntity follower, UserEntity following);

    /** Pending follow requests received by the given user. */
    List<Follow> getPendingRequests(UserEntity user);

    /** Accept a pending follow request. Returns the accepted Follow or null if not found. */
    Follow acceptRequest(Long followId, UserEntity currentUser);

    /** Reject (delete) a pending follow request. */
    void rejectRequest(Long followId, UserEntity currentUser);
}
