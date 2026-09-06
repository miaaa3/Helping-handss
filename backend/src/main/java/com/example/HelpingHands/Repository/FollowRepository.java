package com.example.HelpingHands.Repository;

import com.example.HelpingHands.Entity.Follow;
import com.example.HelpingHands.Entity.FollowStatus;
import com.example.HelpingHands.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {
    boolean existsByFollowerAndFollowing(UserEntity follower, UserEntity following);
    void deleteByFollowerAndFollowing(UserEntity follower, UserEntity following);
    Follow findByFollowerAndFollowing(UserEntity follower, UserEntity following);

    /** Pending follow requests received by a user (they need to accept/reject). */
    List<Follow> findByFollowingAndStatus(UserEntity following, FollowStatus status);

    /** Accepted follows created BY a user (the people they actually follow). */
    List<Follow> findByFollowerAndStatus(UserEntity follower, FollowStatus status);

    /** Check if an ACCEPTED follow relationship exists. */
    boolean existsByFollowerAndFollowingAndStatus(UserEntity follower, UserEntity following, FollowStatus status);
}
