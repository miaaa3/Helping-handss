package com.example.HelpingHands.Entity;

import com.example.HelpingHands.Listener.EntityCreatedTimestampListener;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

/**
 * A volunteer's application/sign-up for an {@link Opportunity}.
 */
@EntityListeners(EntityCreatedTimestampListener.class)
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"volunteer", "opportunity"})
@Entity
@Table(name = "opportunity_applications")
public class OpportunityApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference(value = "applications")
    @ManyToOne
    @JoinColumn(name = "opportunity_id", nullable = false)
    private Opportunity opportunity;

    @JsonBackReference(value = "volunteers")
    @ManyToOne
    @JoinColumn(name = "volunteer_id", nullable = false)
    private Volunteer volunteer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApplicationStatus status = ApplicationStatus.PENDING;

    @CreatedDate
    @Column
    private LocalDateTime createdAt;

    public OpportunityApplication(Opportunity opportunity, Volunteer volunteer) {
        this.opportunity = opportunity;
        this.volunteer = volunteer;
        this.status = ApplicationStatus.PENDING;
    }
}
