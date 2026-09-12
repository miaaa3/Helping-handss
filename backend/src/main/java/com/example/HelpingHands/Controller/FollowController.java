package com.example.HelpingHands.Controller;

import com.example.HelpingHands.DTO.FollowDTO;
import com.example.HelpingHands.DTO.PendingRequestDTO;
import com.example.HelpingHands.Entity.Follow;
import com.example.HelpingHands.Entity.FollowStatus;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Repository.UserRepository;
import com.example.HelpingHands.Service.FollowService;
import com.example.HelpingHands.Service.NotificationService;
import com.example.HelpingHands.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/follow")
public class FollowController {
    private final FollowService followService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    /**
     * Toggle follow/unfollow.
     * - No existing follow  →  create PENDING request
     * - PENDING follow      →  cancel (delete) request
     * - ACCEPTED follow     →  unfollow (delete)
     */
    @PostMapping("/follow")
    public ResponseEntity<?> toggleFollowUser(@RequestParam Long userId, Principal principal) {
        UserEntity follower = userService.findByEmail(principal.getName());
        UserEntity following = userService.getUser(userId);

        if (follower == null || following == null) {
            return ResponseEntity.notFound().build();
        }
        if (follower.getId().equals(following.getId())) {
            return ResponseEntity.badRequest().body(Map.of("error", "You cannot follow yourself."));
        }

        Follow existing = followService.findFollowByFollowerAndFollowing(follower, following);
        FollowDTO followDTO = new FollowDTO();

        if (existing != null) {
            // Cancel pending request OR unfollow accepted
            followService.deleteFollow(existing);
            followDTO.setFollow(existing);
            followDTO.setFollowed(false);
            followDTO.setStatus(null);
            return ResponseEntity.ok(followDTO);
        } else {
            // Send a new follow request (PENDING)
            Follow created = followService.createFollow(follower, following);
            notificationService.createFollowNotification(follower, created);
            followDTO.setFollow(created);
            followDTO.setFollowed(false); // not yet accepted
            followDTO.setStatus("PENDING");
            return ResponseEntity.ok(followDTO);
        }
    }

    /** Get all pending follow requests for the authenticated user. */
    @GetMapping("/requests/pending")
    public ResponseEntity<?> getPendingRequests(Principal principal) {
        UserEntity user = userService.findByEmail(principal.getName());
        List<PendingRequestDTO> pending = followService.getPendingRequests(user).stream()
                .map(f -> new PendingRequestDTO(
                        f.getId(),
                        f.getFollowedAt(),
                        f.getStatus() != null ? f.getStatus().name() : null,
                        new PendingRequestDTO.Follower(
                                f.getFollower().getId(),
                                f.getFollower().getName(),
                                f.getFollower().getProfile())))
                .toList();
        return ResponseEntity.ok(pending);
    }

    /** Accept a pending follow request. */
    @PostMapping("/requests/{followId}/accept")
    public ResponseEntity<?> acceptRequest(@PathVariable Long followId, Principal principal) {
        UserEntity user = userService.findByEmail(principal.getName());
        Follow accepted = followService.acceptRequest(followId, user);
        if (accepted == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("accepted", true, "followId", followId));
    }

    /** Reject (delete) a pending follow request. */
    @PostMapping("/requests/{followId}/reject")
    public ResponseEntity<?> rejectRequest(@PathVariable Long followId, Principal principal) {
        UserEntity user = userService.findByEmail(principal.getName());
        followService.rejectRequest(followId, user);
        return ResponseEntity.ok(Map.of("rejected", true, "followId", followId));
    }

    @GetMapping("/getFollowers")
    public ResponseEntity<List<UserEntity>> getFollowers(Principal principal) {
        UserEntity user = userService.findByEmail(principal.getName());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<UserEntity> followers = followService.getFollowers(user);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/getFollowing")
    public ResponseEntity<?> getFollowing(Principal principal) {
        UserEntity user = userService.findByEmail(principal.getName());
        List<?> usersFollowing = userRepository.findUsersFollowingById(user.getId());

        if (usersFollowing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usersFollowing);
    }

    @DeleteMapping("/unfollow")
    public ResponseEntity<?> unfollowUser(@RequestParam Long userId, Principal principal) {
        UserEntity follower = userService.findByEmail(principal.getName());
        UserEntity following = userService.getUser(userId);

        if (follower == null || following == null) {
            return ResponseEntity.notFound().build();
        }

        followService.unfollow(follower, following);
        return ResponseEntity.ok().build();
    }
}
