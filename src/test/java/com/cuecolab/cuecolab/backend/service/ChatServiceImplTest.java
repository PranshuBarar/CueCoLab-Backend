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

    @Test
    void getChatDetails_returnsConvertedDto() {
        UUID chatId = UUID.randomUUID();
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setChatId(chatId);

        ChatDetailsDTO expectedDto = ChatDetailsDTO.builder().chatId(chatId).build();

        when(chatRepository.findByChatId(chatId)).thenReturn(chatEntity);
        when(convertors.convertChatEntityToChatDetailsDTO(chatEntity)).thenReturn(expectedDto);

        ChatDetailsDTO result = chatService.getChatDetails(chatId);

        assertThat(result).isSameAs(expectedDto);
        verify(chatRepository).findByChatId(chatId);
        verify(convertors).convertChatEntityToChatDetailsDTO(chatEntity);
    }

    @Test
    void createChat_createsChatWithMembersAndReturnsLocalizedMessage() {
        UUID roomId = UUID.randomUUID();
        UUID participantId1 = UUID.randomUUID();
        UUID participantId2 = UUID.randomUUID();
        List<UUID> participantIds = List.of(participantId1, participantId2);

        RoomParticipantEntity participant1 = new RoomParticipantEntity();
        participant1.setRoomParticipantId(participantId1);
        participant1.setChatMemberEntityList(new ArrayList<>());
        RoomParticipantEntity participant2 = new RoomParticipantEntity();
        participant2.setRoomParticipantId(participantId2);
        participant2.setChatMemberEntityList(new ArrayList<>());

        when(roomParticipantRepository.findByRoomParticipantId(participantId1)).thenReturn(participant1);
        when(roomParticipantRepository.findByRoomParticipantId(participantId2)).thenReturn(participant2);

        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setRoomId(roomId);
        roomEntity.setChats(new ArrayList<>());

        when(roomRepository.findByRoomId(roomId)).thenReturn(roomEntity);

        ChatEntity persistedChatEntity = new ChatEntity();
        UUID chatId = UUID.randomUUID();
        persistedChatEntity.setChatId(chatId);
        persistedChatEntity.setChatName("Test Chat");
        persistedChatEntity.setChatType(ChatType.GROUP_CHAT);
        persistedChatEntity.setChatMemberEntityList(new ArrayList<>());

        when(chatRepository.save(any(ChatEntity.class))).thenReturn(persistedChatEntity);
        when(messageSource.getMessage(eq("chat.creation.success"), any(), any(Locale.class)))
                .thenReturn("Chat created successfully");

        String result = chatService.createChat(participantIds, roomId, "Test Chat");

        assertThat(result).isEqualTo("Chat created successfully");
        assertThat(roomEntity.getChats()).hasSize(1);

        verify(roomParticipantRepository).findByRoomParticipantId(participantId1);
        verify(roomParticipantRepository).findByRoomParticipantId(participantId2);
        verify(roomRepository).findByRoomId(roomId);
        verify(chatRepository).save(any(ChatEntity.class));
        verify(roomRepository).save(roomEntity);
        verify(messageSource).getMessage(eq("chat.creation.success"), any(), any(Locale.class));
    }

    @Test
    void deleteChat_deletesByIdAndReturnsLocalizedMessage() {
        UUID chatId = UUID.randomUUID();
        when(messageSource.getMessage(eq("chat.deletion.success"), any(), any(Locale.class)))
                .thenReturn("Chat deleted");

        String result = chatService.DeleteChat(chatId);

        assertThat(result).isEqualTo("Chat deleted");
        verify(chatRepository).deleteByChatId(chatId);
        verify(messageSource).getMessage(eq("chat.deletion.success"), any(), any(Locale.class));
    }
}


