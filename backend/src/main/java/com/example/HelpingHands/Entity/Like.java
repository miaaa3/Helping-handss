package com.example.HelpingHands.Entity;

import com.example.HelpingHands.Listener.EntityCreatedTimestampListener;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"notification","user","post"})
@Builder
@EntityListeners(EntityCreatedTimestampListener.class)
@Entity
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isLiked=true;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne
    @JsonBackReference(value = "likes")
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JsonBackReference(value = "user-like")
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @JsonManagedReference(value = "like-notification")
    @OneToOne(mappedBy = "like", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Notification notification;

}
