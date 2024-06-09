package com.cuecolab.cuecolab.backend.DTOs.roomDTOs.entryDTOs;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class RoomEntryDTO {
    private String roomName;
    private UUID userId;
}
