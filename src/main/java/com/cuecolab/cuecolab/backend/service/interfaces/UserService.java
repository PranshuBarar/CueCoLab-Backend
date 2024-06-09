package com.cuecolab.cuecolab.backend.service.interfaces;

import com.cuecolab.cuecolab.backend.DTOs.destinationDTOs.responseDTOs.DestinationDTO;
import com.cuecolab.cuecolab.backend.DTOs.userDTOs.entryDTOs.UserEntryDTO;
import com.cuecolab.cuecolab.backend.DTOs.userDTOs.responseDTOs.UserAccountDetailsResponseDTO;
import com.cuecolab.cuecolab.backend.DTOs.userDTOs.responseDTOs.UserAccountDetailsResponseLoginDTO;
import com.cuecolab.cuecolab.backend.entities.UserEntity;

import java.util.List;
import java.util.UUID;

public interface UserService {


    UserAccountDetailsResponseDTO getUserAccountDetails(String email); //Get

    String userUpgrade(String email); //Put

    boolean getUserSubscriptionStatus(String email); //Get

    String userAccountDelete(String email); //Delete

    public String userDowngrade(String email); //Put

    UserAccountDetailsResponseLoginDTO getUserAccountLoginDetails();

    List<DestinationDTO> getDestinationList();
}
