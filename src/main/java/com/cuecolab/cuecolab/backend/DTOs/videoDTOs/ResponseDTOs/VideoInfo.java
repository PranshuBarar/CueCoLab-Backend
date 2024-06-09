package com.cuecolab.cuecolab.backend.DTOs.videoDTOs.ResponseDTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoInfo {
    private String title;
    private String description;
    private String tags;
}
