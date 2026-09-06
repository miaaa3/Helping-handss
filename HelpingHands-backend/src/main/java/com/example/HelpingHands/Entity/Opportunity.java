package com.example.HelpingHands.Entity;

import com.example.HelpingHands.Listener.EntityCreatedTimestampListener;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A volunteering opportunity posted by an organization.
 * Volunteers apply via {@link OpportunityApplication}.
 */
@EntityListeners(EntityCreatedTimestampListener.class)
@EqualsAndHashCode(exclude = {"organization", "applications"})
@Data
@NoArgsConstructor
@Entity
@Table(name = "opportunities")
public class Opportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OpportunityCategory category;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false, length = 100)
    private String location;

    @Column(name = "needed_volunteers", nullable = false)
    private int neededVolunteers;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OpportunityStatus status = OpportunityStatus.DRAFT;

    @CreatedDate
    private LocalDateTime createdAt;

    @JsonBackReference(value = "organization")
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @JsonManagedReference(value = "applications")
    @OneToMany(mappedBy = "opportunity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OpportunityApplication> applications = new HashSet<>();

    public Opportunity(String title, String description, OpportunityCategory category,
                        LocalDateTime date, String location, int neededVolunteers, OpportunityStatus status) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.date = date;
        this.location = location;
        this.neededVolunteers = neededVolunteers;
        this.status = status != null ? status : OpportunityStatus.DRAFT;
    }

    public void addApplication(OpportunityApplication application) {
        applications.add(application);
        application.setOpportunity(this);
    }
}
