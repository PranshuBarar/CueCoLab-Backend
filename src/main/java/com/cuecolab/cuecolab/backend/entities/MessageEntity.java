package com.cuecolab.cuecolab.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID messageId;

    @NotNull
    private LocalDateTime clientSideCreationDateTime;

    @NotNull
    private LocalDateTime serverSideCreationDateTime;

    @Size(max = 1000)
    private String content;

    //////////////////////////////////////////////////////////////////
    //===================================
    //Parents of this message
    //===================================
    @ManyToOne
    @JoinColumn
    @NotNull
    @JsonIgnore
    private ChatEntity chatEntity;


    /*In this particular case below, I want to stop cascading. It means,
    if ChatMemberEntity gets deleted, then also message entity
    should persist in the database*/
    @ManyToOne
    @JoinColumn
    @NotNull
    @JsonIgnore
    private ChatMemberEntity chatMemberEntity;

    //////////////////////////////////////////////////////////////////
    //===================================
    //Children of this message
    //===================================
    //MESSAGE-ENTITY HAS NO CHILDREN, AS IT IS THE LAST ENTITY IN
    //THIS APPLICATION
}