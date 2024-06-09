package com.cuecolab.cuecolab.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chatmembers")
public class ChatMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID chatMemberId;

    //////////////////////////////////////////////////////////////////
    //===================================
    //Parents of this chat member
    //===================================
    @ManyToOne
    @JoinColumn
    @NotNull
    @JsonIgnore
    private ChatEntity chatEntity;

    @ManyToOne
    @JoinColumn
    @NotNull
    @JsonIgnore
    private RoomParticipantEntity roomParticipantEntity;

    //////////////////////////////////////////////////////////////////
    //===================================
    //Children of this chat member
    //===================================
    @OneToMany(mappedBy = "chatMemberEntity")
    @JsonIgnore
    private List<MessageEntity> messageEntityList = new ArrayList<>();
}

