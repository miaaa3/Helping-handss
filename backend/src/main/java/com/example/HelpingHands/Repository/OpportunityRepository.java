package com.example.HelpingHands.Repository;

import com.example.HelpingHands.Entity.Opportunity;
import com.example.HelpingHands.Entity.OpportunityCategory;
import com.example.HelpingHands.Entity.OpportunityStatus;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

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

    /** Personalized: OPEN opportunities from VERIFIED orgs whose category is one the volunteer is interested in. */
    @Query("SELECT o FROM Opportunity o WHERE o.status = com.example.HelpingHands.Entity.OpportunityStatus.OPEN " +
            "AND o.organization.verificationStatus = com.example.HelpingHands.Entity.OrganizationVerificationStatus.VERIFIED " +
            "AND o.category IN :categories ORDER BY o.date ASC")
    List<Opportunity> findRecommended(@Param("categories") java.util.Collection<OpportunityCategory> categories);

    /** Verified organizations that posted OPEN opportunities in the given categories, most matches first. */
    @Query("SELECT o.organization FROM Opportunity o " +
            "WHERE o.category IN :categories " +
            "AND o.status = com.example.HelpingHands.Entity.OpportunityStatus.OPEN " +
            "AND o.organization.verificationStatus = com.example.HelpingHands.Entity.OrganizationVerificationStatus.VERIFIED " +
            "GROUP BY o.organization ORDER BY COUNT(o) DESC")
    List<Organization> findSuggestedOrganizationsByCategories(@Param("categories") Set<OpportunityCategory> categories);

    /** Fallback: most active verified organizations overall. */
    @Query("SELECT o.organization FROM Opportunity o " +
            "WHERE o.status = com.example.HelpingHands.Entity.OpportunityStatus.OPEN " +
            "AND o.organization.verificationStatus = com.example.HelpingHands.Entity.OrganizationVerificationStatus.VERIFIED " +
            "GROUP BY o.organization ORDER BY COUNT(o) DESC")
    List<Organization> findMostActiveOrganizations();
}
