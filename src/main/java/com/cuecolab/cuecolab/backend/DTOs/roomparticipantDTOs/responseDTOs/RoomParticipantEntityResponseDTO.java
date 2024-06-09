package com.cuecolab.cuecolab.backend.DTOs.roomparticipantDTOs.responseDTOs;

import com.cuecolab.cuecolab.backend.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class RoomParticipantEntityResponseDTO {
    private UUID roomParticipantId;
    private Role role;
    private UUID roomId;
    private UUID userId;
}
