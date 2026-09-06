package com.example.HelpingHands.Entity;


import com.example.HelpingHands.Listener.EntityCreatedTimestampListener;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Set;

@EntityListeners(EntityCreatedTimestampListener.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"user","media","likes","comments"})
@Entity
@Data
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @JsonBackReference(value = "user-post")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @JsonManagedReference(value = "media")
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private Set<MediaPost> media;

    @JsonManagedReference(value = "likes")
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Like> likes;


    @JsonManagedReference(value = "post-comment")
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Comment> comments;

    public Post(String content) {
        this.content = content;
    }

    public Post(String content, UserEntity user) {
        this.content = content;
        this.user = user;
    }

    public Post(String content, Set<MediaPost> media, UserEntity user) {
        this.content = content;
        this.media = media;
        this.user = user;
    }


}
