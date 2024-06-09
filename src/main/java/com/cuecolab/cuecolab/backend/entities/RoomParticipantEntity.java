package com.cuecolab.cuecolab.backend.entities;


import com.cuecolab.cuecolab.backend.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "room_participants")
public class RoomParticipantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID roomParticipantId;

    private Role role;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime roomParticipantCreationDateTime;

    //////////////////////////////////////////////////////////////////
    //===================================
    //Parents of this room_participant
    //===================================
    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private RoomEntity roomEntity;

    //////////////////////////////////////////////////////////////////
    //===================================
    //Children of this room_participant
    //===================================
    @OneToMany(mappedBy = "roomParticipantEntity", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ChatMemberEntity> chatMemberEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "roomParticipantEntity")
    @JsonIgnore
    private List<VideoEntity> videoEntityList = new ArrayList<>();

}
