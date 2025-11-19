package com.cuecolab.cuecolab.backend.service;

import com.cuecolab.cuecolab.backend.DTOs.destinationDTOs.responseDTOs.DestinationDTO;
import com.cuecolab.cuecolab.backend.DTOs.userDTOs.responseDTOs.UserAccountDetailsResponseDTO;
import com.cuecolab.cuecolab.backend.convertors.Convertors;
import com.cuecolab.cuecolab.backend.entities.DestinationEntity;
import com.cuecolab.cuecolab.backend.entities.UserEntity;
import com.cuecolab.cuecolab.backend.enums.SocialLogin;
import com.cuecolab.cuecolab.backend.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    private UserServiceImpl userService;

    private UserRepository userRepository;
    private MessageSource messageSource;
    private Convertors convertors;

    private MockedStatic<CurrentUserId> currentUserIdMockedStatic;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        messageSource = mock(MessageSource.class);
        convertors = mock(Convertors.class);

        userService = new UserServiceImpl();

        // manually inject mocks into private fields (field injection in production code)
        ReflectionTestUtils.setField(userService, "userRepository", userRepository);
        ReflectionTestUtils.setField(userService, "messageSource", messageSource);
        ReflectionTestUtils.setField(userService, "convertors", convertors);

        currentUserIdMockedStatic = Mockito.mockStatic(CurrentUserId.class);
        LocaleContextHolder.setLocale(Locale.ENGLISH);
    }

    @AfterEach
    void tearDown() {
        if (currentUserIdMockedStatic != null) {
            currentUserIdMockedStatic.close();
        }
    }

    

    @Test
    void getDestinationList_returnsConvertedDestinationDtos() {
        UUID userId = UUID.randomUUID();
        currentUserIdMockedStatic.when(CurrentUserId::getUserId).thenReturn(userId);

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);

        DestinationEntity destinationEntity = new DestinationEntity();
        List<DestinationEntity> destinationEntities = List.of(destinationEntity);
        userEntity.setDestinationEntityList(destinationEntities);

        DestinationDTO destinationDTO = DestinationDTO.builder().build();
        List<DestinationDTO> destinationDTOS = List.of(destinationDTO);

        when(userRepository.findByUserId(userId)).thenReturn(userEntity);
        when(convertors.convertDestinationEntityListToDestinationDTOList(destinationEntities))
                .thenReturn(destinationDTOS);

        List<DestinationDTO> result = userService.getDestinationList();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isSameAs(destinationDTO);
        verify(userRepository).findByUserId(userId);
        verify(convertors).convertDestinationEntityListToDestinationDTOList(destinationEntities);
    }

    @Test
    void getUserSubscriptionStatus_returnsProFlag() {
        UserEntity userEntity = new UserEntity();
        userEntity.setPro(true);

        when(userRepository.findByEmail("test@example.com")).thenReturn(userEntity);

        boolean result = userService.getUserSubscriptionStatus("test@example.com");

        assertThat(result).isTrue();
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void userAccountDelete_deletesByEmailAndReturnsMessage() {
        when(messageSource.getMessage(eq("user.delete.success"), isNull(), any(Locale.class)))
                .thenReturn("User deleted successfully");

        String result = userService.userAccountDelete("delete@example.com");

        assertThat(result).isEqualTo("User deleted successfully");
        verify(userRepository).deleteByEmail("delete@example.com");
    }
}


