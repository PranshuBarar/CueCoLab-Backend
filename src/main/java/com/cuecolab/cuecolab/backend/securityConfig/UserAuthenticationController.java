package com.cuecolab.cuecolab.backend.securityConfig;

import com.cuecolab.cuecolab.backend.DTOs.roomDTOs.responseDTOs.RoomSummaryDTO;
import com.cuecolab.cuecolab.backend.DTOs.userDTOs.responseDTOs.UserAccountDetailsResponseDTO;
import com.cuecolab.cuecolab.backend.DTOs.userDTOs.responseDTOs.UserAccountDetailsResponseLoginDTO;
import com.cuecolab.cuecolab.backend.convertors.Convertors;
import com.cuecolab.cuecolab.backend.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Simplified adapter that maps authenticated {@link UserEntity} objects into the
 * DTOs expected by the login endpoints. The original project relies on a richer
 * security module, but for testing purposes we keep the behaviour minimal and
 * dependency-free.
 */
@Component
public class UserAuthenticationController {

    private final Convertors convertors;

    @Autowired
    public UserAuthenticationController(Convertors convertors) {
        this.convertors = convertors;
    }

    public UserAccountDetailsResponseLoginDTO getUserAccountDetailsResponseLoginDTO(UserEntity userEntity) {
        if (userEntity == null) {
            return UserAccountDetailsResponseLoginDTO.builder()
                    .userAccountDetailsResponseDTO(null)
                    .roomSummaryDTOList(Collections.emptyList())
                    .build();
        }

        UserAccountDetailsResponseDTO userAccountDetails = UserAccountDetailsResponseDTO.builder()
                .userId(userEntity.getUserId())
                .email(userEntity.getEmail())
                .maxStorage(userEntity.getMaxStorage())
                .storageUsed(userEntity.getStorageUsed())
                .isPro(userEntity.isPro())
                .socialLogin(userEntity.getSocialLogin())
                .build();

        List<RoomSummaryDTO> rooms = convertors.convertRoomEntityListToRoomSummaryDTOList(userEntity.getRoomEntityList());

        return UserAccountDetailsResponseLoginDTO.builder()
                .userAccountDetailsResponseDTO(userAccountDetails)
                .roomSummaryDTOList(rooms)
                .build();
    }
}


