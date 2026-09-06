package com.example.HelpingHands.Controller;

import com.example.HelpingHands.DTO.OrganizationUpdateRequest;
import com.example.HelpingHands.DTO.PublicProfileDTO;
import com.example.HelpingHands.DTO.SearchResultWithFollowStatusDTO;
import com.example.HelpingHands.DTO.UserDTO;
import com.example.HelpingHands.DTO.VolunteerUpdateRequest;
import com.example.HelpingHands.Entity.Follow;
import com.example.HelpingHands.Entity.FollowStatus;
import com.example.HelpingHands.Entity.Organization;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Entity.Volunteer;
import com.example.HelpingHands.Repository.UserRepository;
import com.example.HelpingHands.Service.FollowService;
import com.example.HelpingHands.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("api/users")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final FollowService followService;
    private final UserRepository userRepository;
    @GetMapping("/getUser")
    public ResponseEntity<?> getUser(Principal principal) {
        UserEntity user = userService.findByEmail(principal.getName());
        UserDTO userDTO = new UserDTO();
        userDTO.setUser(user);
        userDTO.setNumberOfFollowers(userRepository.numberOfFollowers(user.getId()));
        userDTO.setNumberOfFollowing(userRepository.numberOfFollowing(user.getId()));

        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/updateVolunteer")
    public ResponseEntity<?> updateVolunteer(@RequestBody VolunteerUpdateRequest request, Principal principal) {
        Volunteer updatedVolunteer = userService.updateVolunteer(principal.getName(), request);
        return ResponseEntity.ok(updatedVolunteer);
    }

    @PutMapping("/updateOrganization")
    public ResponseEntity<?> updateOrganization(@RequestBody OrganizationUpdateRequest request, Principal principal) {
        Organization updatedOrganization = userService.updateOrganization(principal.getName(), request);
        return ResponseEntity.ok(updatedOrganization);
    }


    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String keyword, Principal principal) {
        UserEntity user = userService.findByEmail(principal.getName());
        List<UserEntity> searchResults = userService.searchUsers(keyword,user.getName());
        List<SearchResultWithFollowStatusDTO> resultsWithFollowStatus = new ArrayList<>();

        for (UserEntity searchResult : searchResults) {
            boolean isFollowing = followService.isFollowing(user, searchResult);

            SearchResultWithFollowStatusDTO resultDTO = new SearchResultWithFollowStatusDTO();
            resultDTO.setUser(searchResult);
            resultDTO.setFollowed(isFollowing);

            resultsWithFollowStatus.add(resultDTO);
        }
        return ResponseEntity.ok(resultsWithFollowStatus);
    }

    @GetMapping("/suggestions")
    public ResponseEntity<?> suggestions(Principal principal) {
        UserEntity user = userService.findByEmail(principal.getName());
        List<UserEntity> suggestions = userService.getSuggestedUsers(user.getId());
        return ResponseEntity.ok(suggestions);
    }

    /** Public profile for any user - used by the "view profile" pages (own or someone else's). */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id, Principal principal) {
        UserEntity currentUser = userService.findByEmail(principal.getName());
        UserEntity target = userService.getUser(id);

        PublicProfileDTO profileDTO = new PublicProfileDTO();
        profileDTO.setUser(target);
        profileDTO.setNumberOfFollowers(userRepository.numberOfFollowers(target.getId()));
        profileDTO.setNumberOfFollowing(userRepository.numberOfFollowing(target.getId()));
        profileDTO.setOwnProfile(target.getId().equals(currentUser.getId()));

        if (!profileDTO.isOwnProfile()) {
            Follow existingFollow = followService.findFollowByFollowerAndFollowing(currentUser, target);
            if (existingFollow != null) {
                profileDTO.setFollowStatus(existingFollow.getStatus().name());
                profileDTO.setFollowing(existingFollow.getStatus() == FollowStatus.ACCEPTED);
            }
        }

        return ResponseEntity.ok(profileDTO);
    }

}
