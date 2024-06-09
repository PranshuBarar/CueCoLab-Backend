package com.cuecolab.cuecolab.backend.DTOs.videoDTOs.EntryDTOs;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoUploadToDestinationRequestDTO {
    private String videoId;
    private String destinationId;
    private String privacyStatus;
}
