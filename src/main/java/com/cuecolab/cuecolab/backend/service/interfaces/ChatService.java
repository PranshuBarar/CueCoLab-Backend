package com.cuecolab.cuecolab.backend.service.interfaces;

import com.cuecolab.cuecolab.backend.DTOs.chatDTOs.responseDTOs.ChatDetailsDTO;
import com.cuecolab.cuecolab.backend.entities.ChatEntity;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    ChatDetailsDTO getChatDetails(UUID chatId);

    String createChat(List<UUID> roomMemberIDs, UUID roomId, String chatName);

    String DeleteChat(UUID chatId);

}
