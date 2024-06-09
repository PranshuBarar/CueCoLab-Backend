package com.cuecolab.cuecolab.backend.DTOs.userDTOs.entryDTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEntryDTO {
    private String email;
    private String username;
}
