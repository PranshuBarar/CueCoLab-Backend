package com.cuecolab.cuecolab.backend.DTOs.videoDTOs;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Data
@Getter
@Setter
public class VideoUploadMessageToSQSDTO {
    private String videoId;
    private String destinationId;
    private String accessToken;
    private String clientId;
    private String clientSecret;
    private String s3Path;
    private String videoTitle;
    private String videoDescription;
    private String privacyStatus;
    private String tags;
    private String bucketName;
}
