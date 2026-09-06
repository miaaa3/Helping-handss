package com.example.HelpingHands.Repository;

import com.example.HelpingHands.Entity.Donation;
import com.example.HelpingHands.Entity.DonationStatus;
import com.example.HelpingHands.Entity.Organization;
import com.example.HelpingHands.Entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

    List<Donation> findByOrganizationAndStatusOrderByCreatedAtDesc(Organization organization, DonationStatus status);

    List<Donation> findByDonorOrderByCreatedAtDesc(UserEntity donor);

    Optional<Donation> findByStripePaymentIntentId(String stripePaymentIntentId);

    @Query("SELECT COALESCE(SUM(d.amount), 0) FROM Donation d " +
            "WHERE d.organization.id = :organizationId AND d.status = 'SUCCEEDED'")
    BigDecimal getTotalRaisedForOrganization(@Param("organizationId") Long organizationId);

    /** All donations platform-wide, newest first - for the admin donation activity view. */
    Page<Donation> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
