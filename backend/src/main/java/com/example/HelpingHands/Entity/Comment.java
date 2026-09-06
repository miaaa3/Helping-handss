package com.example.HelpingHands.Entity;

import com.example.HelpingHands.Listener.EntityCreatedTimestampListener;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@EntityListeners(EntityCreatedTimestampListener.class)
@EqualsAndHashCode(exclude = {"notification","post"})
@NoArgsConstructor
@Data
@Table(name = "comments")
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @JsonBackReference(value = "post-comment")
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;


    @OneToOne(mappedBy = "comment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Notification notification;

}
