package com.example.HelpingHands.Service;

import com.example.HelpingHands.DTO.OrganizationUpdateRequest;
import com.example.HelpingHands.DTO.VolunteerUpdateRequest;
import com.example.HelpingHands.Entity.Organization;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Entity.Volunteer;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserEntity createVolunteer(Volunteer volunteer);

    UserEntity createOrganization(Organization organization);

    UserEntity getUser(Long id);

    UserEntity findByEmail(String email);
    List<UserEntity> searchUsers(String keyword, String authenticatedUsername);

    List<UserEntity> getSuggestedUsers(Long userId);

    Volunteer updateVolunteer(String email, VolunteerUpdateRequest request);

    Organization updateOrganization(String email, OrganizationUpdateRequest request);
}
