package com.cuecolab.cuecolab.backend.DTOs.chatmemberDTOs.responseDTOs;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class ChatMemberDTO {
    private UUID chatMemberId;
    private String chatMemberName;
}
