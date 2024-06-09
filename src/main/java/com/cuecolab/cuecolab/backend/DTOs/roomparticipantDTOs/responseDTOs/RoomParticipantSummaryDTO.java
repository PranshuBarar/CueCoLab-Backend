package com.cuecolab.cuecolab.backend.DTOs.roomparticipantDTOs.responseDTOs;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class RoomParticipantSummaryDTO {
    private UUID roomParticipantId;
    private String roomParticipantEmail;
}
