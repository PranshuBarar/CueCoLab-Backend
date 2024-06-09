package com.cuecolab.cuecolab.backend.DTOs.roomDTOs.responseDTOs;

import com.cuecolab.cuecolab.backend.DTOs.chatDTOs.responseDTOs.ChatSummaryDTO;
import com.cuecolab.cuecolab.backend.DTOs.roomparticipantDTOs.responseDTOs.RoomParticipantSummaryDTO;
import com.cuecolab.cuecolab.backend.DTOs.videoDTOs.ResponseDTOs.VideoSummaryDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Builder
@Data
public class RoomResponseDTO {
    private UUID roomId;
    private String roomName;
    private List<ChatSummaryDTO> chats;
    private List<VideoSummaryDTO> videos;
    private List<RoomParticipantSummaryDTO> participants;
}
