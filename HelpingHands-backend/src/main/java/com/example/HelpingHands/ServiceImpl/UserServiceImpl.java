package com.example.HelpingHands.ServiceImpl;

import com.example.HelpingHands.DTO.OrganizationUpdateRequest;
import com.example.HelpingHands.DTO.VolunteerUpdateRequest;
import com.example.HelpingHands.Entity.Organization;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Entity.Volunteer;
import com.example.HelpingHands.Repository.OrganizationRepository;
import com.example.HelpingHands.Repository.UserRepository;
import com.example.HelpingHands.Repository.VolunteerRepository;
import com.example.HelpingHands.Service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final VolunteerRepository volunteerRepository;

    @Override
    public UserEntity createVolunteer(Volunteer volunteer) {
        volunteer.setPassword(passwordEncoder.encode(volunteer.getPassword()));
        return volunteerRepository.save(volunteer);
    }

    @Override
    public UserEntity createOrganization(Organization organization) {
        organization.setPassword(passwordEncoder.encode(organization.getPassword()));
        return userRepository.save(organization);
    }

    @Override
    public UserEntity getUser(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("User not found"));
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("User not found"));
    }

    public List<UserEntity> searchUsers(String keyword, String authenticatedUsername) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return Collections.emptyList();
        }

        List<UserEntity> users = userRepository.findTop15ByNameContainingIgnoreCaseOrderByName(keyword.trim());
        users = users.stream()
                .filter(user -> !user.getName().equals(authenticatedUsername))
                .collect(Collectors.toList());
        return users;
    }

    @Override
    public List<UserEntity> getSuggestedUsers(Long userId) {
        // Load each user by ID so Hibernate resolves the JOINED-inheritance subclass correctly.
        return userRepository.findSuggestedUserIds(userId).stream()
                .map(id -> userRepository.findById(id).orElse(null))
                .filter(u -> u != null)
                .collect(Collectors.toList());
    }

    @Override
    public Volunteer updateVolunteer(String email, VolunteerUpdateRequest request) {
        Volunteer volunteer = volunteerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Volunteer not found"));

        if (StringUtils.hasText(request.getFullName())) {
            volunteer.setFullName(request.getFullName());
        }
        if (StringUtils.hasText(request.getPhone())) {
            volunteer.setPhone(request.getPhone());
        }
        if (StringUtils.hasText(request.getAddress())) {
            volunteer.setAddress(request.getAddress());
        }
        if (StringUtils.hasText(request.getProfile())) {
            volunteer.setProfile(request.getProfile());
        }
        if (request.getBio() != null) {
            volunteer.setBio(request.getBio());
        }
        if (request.getInterests() != null) {
            volunteer.setInterests(request.getInterests());
        }

        return volunteerRepository.save(volunteer);
    }

    @Override
    public Organization updateOrganization(String email, OrganizationUpdateRequest request) {
        Organization organization = organizationRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Organization not found"));

        if (StringUtils.hasText(request.getPhone())) {
            organization.setPhone(request.getPhone());
        }
        if (StringUtils.hasText(request.getAddress())) {
            organization.setAddress(request.getAddress());
        }
        if (StringUtils.hasText(request.getProfile())) {
            organization.setProfile(request.getProfile());
        }
        if (request.getBio() != null) {
            organization.setBio(request.getBio());
        }
        if (request.getDescription() != null) {
            organization.setDescription(request.getDescription());
        }
        if (StringUtils.hasText(request.getWebsite())) {
            organization.setWebsite(request.getWebsite());
        }
        if (StringUtils.hasText(request.getType())) {
            organization.setType(request.getType());
        }
        if (StringUtils.hasText(request.getFounder())) {
            organization.setFounder(request.getFounder());
        }
        if (request.getCampaignGoal() != null) {
            organization.setCampaignGoal(request.getCampaignGoal());
        }

        return organizationRepository.save(organization);
    }

}
