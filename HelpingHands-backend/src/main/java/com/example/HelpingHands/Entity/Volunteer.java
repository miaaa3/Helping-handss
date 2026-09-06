package com.example.HelpingHands.Entity;

import com.example.HelpingHands.Listener.EntityCreatedTimestampListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.*;
@EqualsAndHashCode(callSuper = true,exclude = "applications")
@NoArgsConstructor
@DiscriminatorValue("volunteer")
@Data
@Entity
@Table(name = "volunteers")
public class Volunteer extends UserEntity {
    private String fullName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Date birthdate;

    private String phone;

    @ElementCollection
    @CollectionTable(name = "volunteer_interests", joinColumns = @JoinColumn(name = "volunteer_id"))
    @Column(name = "interests")
    private Set<String> interests ;

    @JsonIgnore
    @OneToMany(mappedBy = "volunteer")
    private Set<OpportunityApplication> applications;

    public Volunteer(String email, String password, String address,
                     String phone, String role,String name, String fullName,
                     Gender gender, Date birthdate) {
        super(email, password,address,phone, role,name);
        this.fullName=fullName;
        this.gender = gender;
        this.birthdate = birthdate;
    }
}
