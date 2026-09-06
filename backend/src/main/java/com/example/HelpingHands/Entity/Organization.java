package com.example.HelpingHands.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;


@EqualsAndHashCode(callSuper = true,exclude = "opportunities")
@DiscriminatorValue("organization")
@Data
@NoArgsConstructor
@Entity
@Table(name = "organizations")
public class Organization extends UserEntity {
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length=30)
    private String website;

    @Column(length=40)
    private String type;

    @Column(length=20)
    private String founder;

    @Column(name = "founded_at")
    private Date foundedAt;

    /** Optional fundraising goal for this organization's donation progress bar. */
    @Column(name = "campaign_goal", precision = 12, scale = 2)
    private BigDecimal campaignGoal;

    /** Moderation status; defaults to PENDING until an admin verifies the organization. */
    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false, length = 20)
    private OrganizationVerificationStatus verificationStatus = OrganizationVerificationStatus.PENDING;

    @JsonManagedReference(value = "organization")
    @OneToMany(mappedBy = "organization")
    private Set<Opportunity> opportunities;
    public Organization(String email, String password, String address, String phone, String role,String name , String description, String type, String founder, Date foundedAt) {
        super(email, password, address, phone,role,name);
        this.description = description;
        this.type = type;
        this.founder = founder;
        this.foundedAt = foundedAt;
    }
}
