package com.cuecolab.cuecolab.backend.DTOs.videoDTOs.ResponseDTOs;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class VideoSummaryDTO {
    private UUID videoId;
    private String videoFileName;
    private String videoThumbnailS3URI;
    private boolean isLocked;
    private String uploadedBy;
}

