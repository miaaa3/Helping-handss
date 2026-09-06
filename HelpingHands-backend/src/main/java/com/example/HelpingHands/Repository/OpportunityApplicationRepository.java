package com.example.HelpingHands.Repository;

import com.example.HelpingHands.Entity.ApplicationStatus;
import com.example.HelpingHands.Entity.OpportunityApplication;
import com.example.HelpingHands.Entity.OpportunityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OpportunityApplicationRepository extends JpaRepository<OpportunityApplication, Long> {

    /** A volunteer's own applications, newest first - for the volunteer dashboard. */
    List<OpportunityApplication> findByVolunteerIdOrderByCreatedAtDesc(Long volunteerId);

    /** All applicants for one opportunity, newest first - for the organization dashboard. */
    List<OpportunityApplication> findByOpportunityIdOrderByCreatedAtDesc(Long opportunityId);

    /** Used to block re-applying while an application in one of {@code statuses} already exists (e.g. PENDING/ACCEPTED). */
    @Query("SELECT COUNT(a) > 0 FROM OpportunityApplication a " +
            "WHERE a.opportunity.id = :opportunityId AND a.volunteer.id = :volunteerId " +
            "AND a.status IN :statuses")
    boolean existsByOpportunityAndVolunteerAndStatusIn(@Param("opportunityId") Long opportunityId,
                                                         @Param("volunteerId") Long volunteerId,
                                                         @Param("statuses") List<ApplicationStatus> statuses);

    long countByVolunteerIdAndStatus(Long volunteerId, ApplicationStatus status);

    long countByOpportunityOrganizationIdAndStatus(Long organizationId, ApplicationStatus status);

    /** Accepted applications whose opportunity hasn't happened yet, soonest first - "upcoming" on the volunteer dashboard. */
    @Query("SELECT a FROM OpportunityApplication a " +
            "WHERE a.volunteer.id = :volunteerId AND a.status = :status AND a.opportunity.date >= :now " +
            "ORDER BY a.opportunity.date ASC")
    List<OpportunityApplication> findUpcomingByVolunteerAndStatus(@Param("volunteerId") Long volunteerId,
                                                                     @Param("status") ApplicationStatus status,
                                                                     @Param("now") LocalDateTime now);

    /** Accepted applications for opportunities that have already closed - impact stat on the volunteer dashboard. */
    long countByVolunteerIdAndStatusAndOpportunityStatus(Long volunteerId, ApplicationStatus status, OpportunityStatus opportunityStatus);
}
