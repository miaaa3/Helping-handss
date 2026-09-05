package com.example.HelpingHands.Repository;

import com.example.HelpingHands.Entity.Organization;
import com.example.HelpingHands.Entity.OrganizationVerificationStatus;
import com.example.HelpingHands.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization,Long> {
    Optional<Organization> findByEmail(String email);
    List<Organization> findByVerificationStatus(OrganizationVerificationStatus status);
}
