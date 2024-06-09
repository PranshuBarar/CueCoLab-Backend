package com.cuecolab.cuecolab.backend.entities;

import com.cuecolab.cuecolab.backend.enums.ChatType;
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
@Table(name = "chats")
public class ChatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID chatId;

    private String chatName;

    private ChatType chatType;


    //////////////////////////////////////////////////////////////////
    //===================================
    //Parents of this Chat
    //===================================
    @ManyToOne
    @JoinColumn
    @NotNull
    @JsonIgnore
    private RoomEntity roomEntity;


    //////////////////////////////////////////////////////////////////
    //===================================
    //Children of this Chat
    //===================================
    @OneToMany(mappedBy = "chatEntity")
    @JsonIgnore
    private List<MessageEntity> messageEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "chatEntity", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ChatMemberEntity> chatMemberEntityList = new ArrayList<>();

}
