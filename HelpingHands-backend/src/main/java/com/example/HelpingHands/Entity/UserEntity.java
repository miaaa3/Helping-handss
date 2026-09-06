package com.example.HelpingHands.Entity;

import com.example.HelpingHands.Listener.EntityCreatedTimestampListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(EntityCreatedTimestampListener.class)
@EqualsAndHashCode(exclude = {"notifications","followers","following","posts","comments","likes"})
@JsonIgnoreProperties({"notifications","followers","following","posts","comments","likes"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true , nullable = false)
    private String email;

    @Column(unique = true , nullable = false)
    private String name;

    @JsonIgnore
    @Column( nullable = false)
    private String password;

    private String address;
    private String phone;
    private String role;
    private String profile="https://avatars.githubusercontent.com/u/67946056?v=4";

    /** Account moderation flag. Disabled accounts can't authenticate. Managed by admins. */
    @Column(nullable = false)
    private boolean enabled = true;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @JsonManagedReference(value = "followers")
    @OneToMany(mappedBy = "follower")
    private Set<Follow> followers = new HashSet<>();

    @JsonManagedReference(value = "following")
    @OneToMany(mappedBy = "following")
    private Set<Follow> following = new HashSet<>();

    @JsonManagedReference(value = "user-post")
    @OneToMany(mappedBy = "user")
    private Set<Post> posts;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Comment> comments;

    @JsonManagedReference(value = "user-like")
    @OneToMany(mappedBy = "user")
    private Set<Like> likes;

    @JsonManagedReference(value = "user-notification")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Notification> notifications;

    public UserEntity(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;

    }

    public UserEntity(String email, String password, String address, String phone, String role,String name) {
        this.email = email;
        this.password = password;
        this.address=address;
        this.phone=phone;
        this.role=role;
        this.name=name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(getRole()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
