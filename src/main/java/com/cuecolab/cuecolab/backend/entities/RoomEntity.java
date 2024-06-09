package com.cuecolab.cuecolab.backend.entities;

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
import java.util.Objects;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rooms")
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID roomId;

    @NotNull
    private String roomName;


    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime roomCreationDateTime;


    //////////////////////////////////////////////////////////////////
    //===================================
    //Parents of this room
    //===================================
    //This is the user to whom this room belongs to
    @ManyToOne
    @JoinColumn
    @NotNull
    @JsonIgnore
    private UserEntity userEntity;


    //////////////////////////////////////////////////////////////////
    //===================================
    //Children of this room
    //===================================
    //Chats which belong to this room
    @OneToMany(mappedBy = "roomEntity", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ChatEntity> chats =  new ArrayList<>();

    //Videos which belong to this room
    @OneToMany(mappedBy = "roomEntity", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<VideoEntity> videos = new ArrayList<>();

    //Participants which belong to this room
    @OneToMany(mappedBy = "roomEntity", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RoomParticipantEntity> roomParticipants = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomEntity that = (RoomEntity) o;
        return Objects.equals(roomId, that.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(roomId);
    }
}
