package com.cuecolab.cuecolab.backend.DTOs.roomDTOs.responseDTOs;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RoomInvitesSummaryDTO {
    private RoomSummaryDTO roomSummaryDTO;
    private String hostEmail;
}
