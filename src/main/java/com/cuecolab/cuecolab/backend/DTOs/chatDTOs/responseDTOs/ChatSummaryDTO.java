package com.cuecolab.cuecolab.backend.DTOs.chatDTOs.responseDTOs;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class ChatSummaryDTO {
    private UUID chatId;
    private String chatName;
}
