package com.cuecolab.cuecolab.backend.DTOs.videoDTOs.EntryDTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoUpdateDetailsDTO {
    private String videoId;
}
