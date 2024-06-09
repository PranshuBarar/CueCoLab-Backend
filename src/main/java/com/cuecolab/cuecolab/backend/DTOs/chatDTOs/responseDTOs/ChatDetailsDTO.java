package com.cuecolab.cuecolab.backend.DTOs.chatDTOs.responseDTOs;

import com.cuecolab.cuecolab.backend.DTOs.chatmemberDTOs.responseDTOs.ChatMemberDTO;
import com.cuecolab.cuecolab.backend.DTOs.messageDTOs.responseDTOs.MessageResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Builder
@Data
public class ChatDetailsDTO {
    private UUID chatId;
    private String chatName;
    private List<MessageResponseDTO> messages;
    private List<ChatMemberDTO> chatMembers;
}
