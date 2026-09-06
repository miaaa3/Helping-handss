package com.example.HelpingHands.Repository;

import com.example.HelpingHands.Entity.Opportunity;
import com.example.HelpingHands.Entity.OpportunityCategory;
import com.example.HelpingHands.Entity.OpportunityStatus;
import com.example.HelpingHands.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {

    List<Opportunity> findByOrganizationOrderByDateAsc(UserEntity organization);

    // Browse/filter query for the discovery page and feed cards.
    // Any of category/status/location may be null to skip that filter.
    @Query("SELECT o FROM Opportunity o WHERE " +
            "(:category IS NULL OR o.category = :category) AND " +
            "(:status IS NULL OR o.status = :status) AND " +
            "(:location IS NULL OR LOWER(o.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
            "ORDER BY o.date ASC")
    List<Opportunity> search(@Param("category") OpportunityCategory category,
                              @Param("status") OpportunityStatus status,
                              @Param("location") String location);

    // Opportunities from a set of organizations (e.g. followed orgs), excluding drafts.
    @Query("SELECT o FROM Opportunity o WHERE o.organization IN :organizations " +
            "AND o.status <> 'DRAFT' ORDER BY o.date ASC")
    List<Opportunity> findVisibleByOrganizationIn(@Param("organizations") List<UserEntity> organizations);

    /** All opportunities platform-wide, including drafts, newest first - for the admin moderation view. */
    List<Opportunity> findAllByOrderByCreatedAtDesc();

    /** Public discovery: only OPEN opportunities from VERIFIED organizations, soonest first. */
    @Query("SELECT o FROM Opportunity o WHERE o.status = com.example.HelpingHands.Entity.OpportunityStatus.OPEN " +
            "AND o.organization.verificationStatus = com.example.HelpingHands.Entity.OrganizationVerificationStatus.VERIFIED " +
            "ORDER BY o.date ASC")
    List<Opportunity> findPublicOpportunities();
}
