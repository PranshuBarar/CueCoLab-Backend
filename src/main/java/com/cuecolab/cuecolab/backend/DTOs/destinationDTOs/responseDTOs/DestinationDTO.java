package com.cuecolab.cuecolab.backend.DTOs.destinationDTOs.responseDTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
public class DestinationDTO {
    private UUID destinationId;
    private String destinationType;
    private String destinationUserName;
}
