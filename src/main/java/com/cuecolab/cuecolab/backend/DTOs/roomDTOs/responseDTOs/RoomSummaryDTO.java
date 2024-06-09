package com.cuecolab.cuecolab.backend.DTOs.roomDTOs.responseDTOs;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;
@Builder
@Data
public class RoomSummaryDTO {
    private UUID roomId;
    private String roomName;
}
