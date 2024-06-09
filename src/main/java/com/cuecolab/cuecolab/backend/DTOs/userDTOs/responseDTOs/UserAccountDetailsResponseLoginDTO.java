package com.cuecolab.cuecolab.backend.DTOs.userDTOs.responseDTOs;

import com.cuecolab.cuecolab.backend.DTOs.roomDTOs.responseDTOs.RoomSummaryDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserAccountDetailsResponseLoginDTO {
    private UserAccountDetailsResponseDTO userAccountDetailsResponseDTO;
    private List<RoomSummaryDTO> roomSummaryDTOList;
}
