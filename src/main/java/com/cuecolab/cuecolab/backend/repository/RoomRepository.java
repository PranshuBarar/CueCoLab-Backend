package com.cuecolab.cuecolab.backend.repository;

import com.cuecolab.cuecolab.backend.entities.RoomEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, UUID>  {

    List<RoomEntity> findAllByUserEntity_UserId(UUID userId);

    RoomEntity findByRoomId(UUID roomId);

    void deleteByRoomId(UUID roomId);
}
