package com.cuecolab.cuecolab.backend.repository;

import com.cuecolab.cuecolab.backend.entities.RoomParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoomParticipantRepository extends JpaRepository<RoomParticipantEntity, UUID> {

    void deleteByRoomParticipantId(UUID roomParticipantId);

    RoomParticipantEntity findByRoomParticipantId(UUID roomParticipantId);

    RoomParticipantEntity findByRoomEntityRoomIdAndUserEntityUserId(UUID roomId, UUID userId);
}
