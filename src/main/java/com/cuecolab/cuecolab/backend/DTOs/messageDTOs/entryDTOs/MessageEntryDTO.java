package com.cuecolab.cuecolab.backend.DTOs.messageDTOs.entryDTOs;

import com.cuecolab.cuecolab.backend.entities.ChatMemberEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class MessageEntryDTO {
    String messageContent;
    UUID chatMemberId;
    UUID chatId;
    LocalDateTime clientSideDateTime;
}
