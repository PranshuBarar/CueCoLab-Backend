package com.cuecolab.cuecolab.backend.service;

import com.cuecolab.cuecolab.backend.DTOs.destinationDTOs.responseDTOs.DestinationDTO;
import com.cuecolab.cuecolab.backend.DTOs.userDTOs.responseDTOs.UserAccountDetailsResponseDTO;
import com.cuecolab.cuecolab.backend.DTOs.userDTOs.responseDTOs.UserAccountDetailsResponseLoginDTO;
import com.cuecolab.cuecolab.backend.convertors.Convertors;
import com.cuecolab.cuecolab.backend.entities.DestinationEntity;
import com.cuecolab.cuecolab.backend.entities.UserEntity;
import com.cuecolab.cuecolab.backend.repository.UserRepository;
import com.cuecolab.cuecolab.backend.securityConfig.UserAuthenticationController;
import com.cuecolab.cuecolab.backend.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserAuthenticationController userAuthenticationController;

    @Autowired
    private Convertors convertors;

    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    //All these methods required custom security checks for authorisation
    //It has to be comprehensively checked that whether the user how is making the request
    //has the right permissions and authority to do so. I will implement custom authorisation
    //logic in each and every method here to ensure that the user has the right permissions.
    //But right now I am not implementing those things and implementing just core function logic for now
    //At the time of setup of authentication via spring security, custom authorisation will be implemented
    //here /*
    //        Now we will map each of this roomEntity (in this roomEntityList) to a RoomResponseDTO
    //        to send a summarised data to the frontend so that frontend can be able to show the first look of the room
    //        to the user. If user clicks further on any of the resource then again the request will come to the frontend
    //        with the UUID of that resource. Backend will again fetch the data using that UUID, and again the same principle
    //        of minimum data required to show the first of that resource
    //        */

    @Override //Get
    @Transactional
    public UserAccountDetailsResponseDTO getUserAccountDetails(String email) {

        //We will fetch the user entity from the repository and convert it into dto and return it
        UserEntity userEntity = userRepository.findByEmail(email);

        return UserAccountDetailsResponseDTO.builder()
                .userId(userEntity.getUserId())
                .email(userEntity.getEmail())
                .maxStorage(userEntity.getMaxStorage())
                .storageUsed(userEntity.getStorageUsed())
                .socialLogin(userEntity.getSocialLogin())
                .isPro(userEntity.isPro())
                .build();

    }

    @Override //Put
    @Transactional
    public String userUpgrade(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        userEntity.setPro(true);
        userRepository.save(userEntity);
        return messageSource.getMessage("user.upgrade.success", null, LocaleContextHolder.getLocale());
    }

    @Override //Put
    @Transactional
    public String userDowngrade(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        userEntity.setPro(false);
        userRepository.save(userEntity);
        return messageSource.getMessage("user.downgrade.success", null, LocaleContextHolder.getLocale());
    }

    @Override
    @Transactional
    public UserAccountDetailsResponseLoginDTO getUserAccountLoginDetails() {
        UUID userId = CurrentUserId.getUserId();
        UserEntity userEntity = userRepository.findByUserId(userId);
        System.out.println("This is userId : " + userId);

        return userAuthenticationController.getUserAccountDetailsResponseLoginDTO(userEntity);
    }

    @Override
    @Transactional
    public List<DestinationDTO> getDestinationList() {
        UUID userId = CurrentUserId.getUserId();
        UserEntity userEntity = userRepository.findByUserId(userId);
        List<DestinationEntity> destinationEntityList = userEntity.getDestinationEntityList();
        return convertors.convertDestinationEntityListToDestinationDTOList(destinationEntityList);

    }

    @Override //Get
    @Transactional
    public boolean getUserSubscriptionStatus(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        return userEntity.isPro();
    }

    @Override //Delete
    @Transactional
    public String userAccountDelete(String email) {
        userRepository.deleteByEmail(email);
        return messageSource.getMessage("user.delete.success", null, LocaleContextHolder.getLocale());
    }
}

//See when a user upgrades from free to pro, then the upgrade options and buttons will
//be removed, till the subscription expires. Or the button and options will appear just
//before 2-3 days of expiration

