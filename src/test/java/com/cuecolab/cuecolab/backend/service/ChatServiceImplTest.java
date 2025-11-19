package com.cuecolab.cuecolab.backend.service;

import com.cuecolab.cuecolab.backend.DTOs.chatDTOs.responseDTOs.ChatDetailsDTO;
import com.cuecolab.cuecolab.backend.convertors.Convertors;
import com.cuecolab.cuecolab.backend.entities.ChatEntity;
import com.cuecolab.cuecolab.backend.entities.RoomEntity;
import com.cuecolab.cuecolab.backend.entities.RoomParticipantEntity;
import com.cuecolab.cuecolab.backend.enums.ChatType;
import com.cuecolab.cuecolab.backend.repository.ChatRepository;
import com.cuecolab.cuecolab.backend.repository.RoomParticipantRepository;
import com.cuecolab.cuecolab.backend.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ChatServiceImplTest {

    private ChatServiceImpl chatService;

    private RoomParticipantRepository roomParticipantRepository;
    private RoomRepository roomRepository;
    private ChatRepository chatRepository;
    private Convertors convertors;
    private MessageSource messageSource;

    @BeforeEach
    void setUp() {
        roomParticipantRepository = mock(RoomParticipantRepository.class);
        roomRepository = mock(RoomRepository.class);
        chatRepository = mock(ChatRepository.class);
        convertors = mock(Convertors.class);
        messageSource = mock(MessageSource.class);

        chatService = new ChatServiceImpl();
        ReflectionTestUtils.setField(chatService, "roomParticipantRepository", roomParticipantRepository);
        ReflectionTestUtils.setField(chatService, "roomRepository", roomRepository);
        ReflectionTestUtils.setField(chatService, "chatRepository", chatRepository);
        ReflectionTestUtils.setField(chatService, "convertors", convertors);
        ReflectionTestUtils.setField(chatService, "messageSource", messageSource);

        LocaleContextHolder.setLocale(Locale.ENGLISH);
    }


    
}


