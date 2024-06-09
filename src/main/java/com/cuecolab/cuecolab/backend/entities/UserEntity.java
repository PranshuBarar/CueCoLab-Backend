package com.cuecolab.cuecolab.backend.entities;


import com.cuecolab.cuecolab.backend.enums.SocialLogin;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID userId;

    //This can be null if logged in directly via putting email address and otp to get logged in
    //Hence I have not put @NotNull here
    private SocialLogin socialLogin;

    @NotNull
    @Email
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @NotNull
    private boolean isPro;

    @NotNull
    private int storageUsed = 0;

    @NotNull
    private int maxStorage  = 100;


    //////////////////////////////////////////////////////////////////
    //===================================
    //Parents of this user
    //===================================
    //USER-ENTITY HAS NOT PARENTS, AS IT IS ITSELF PARENT OF ALL ENTITIES
    //IN THIS APPLICATION


    //////////////////////////////////////////////////////////////////
    //===================================
    //Children of this user
    //===================================
    //All the rooms which belong to this userEntity
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    @JsonIgnore
    @OrderBy("roomCreationDateTime")
    private List<RoomEntity> roomEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    @JsonIgnore
    @OrderBy("roomParticipantCreationDateTime")
    private List<RoomParticipantEntity> roomParticipantEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "userEntity")
    @JsonIgnore
    private List<TokenEntity> tokensIssuedToThisUser = new ArrayList<>();

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<DestinationEntity> destinationEntityList = new ArrayList<>();
}