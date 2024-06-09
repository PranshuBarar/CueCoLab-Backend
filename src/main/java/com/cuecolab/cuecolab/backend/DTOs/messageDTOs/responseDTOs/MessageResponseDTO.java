package com.cuecolab.cuecolab.backend.DTOs.messageDTOs.responseDTOs;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class MessageResponseDTO {
    private UUID messageId;
    private String senderName;
    private LocalDateTime timestamp;
    private String content;
}
