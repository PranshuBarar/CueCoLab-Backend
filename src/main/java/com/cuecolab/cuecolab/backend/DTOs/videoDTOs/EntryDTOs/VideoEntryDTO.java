package com.cuecolab.cuecolab.backend.DTOs.videoDTOs.EntryDTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoEntryDTO {
    private String RoomId;
    private String uploader_UserId;
    private String videoFileName;
    private int videoFileSize;
}
