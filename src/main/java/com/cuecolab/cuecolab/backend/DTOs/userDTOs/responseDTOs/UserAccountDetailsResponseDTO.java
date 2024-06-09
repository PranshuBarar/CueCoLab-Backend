package com.cuecolab.cuecolab.backend.DTOs.userDTOs.responseDTOs;

import com.cuecolab.cuecolab.backend.enums.SocialLogin;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;


@Data
@Builder
public class UserAccountDetailsResponseDTO {
    private UUID userId;
    private String email;
    private SocialLogin socialLogin;
    private boolean isPro;
    private double storageUsed;
    private double maxStorage;
    private String destination;
}
