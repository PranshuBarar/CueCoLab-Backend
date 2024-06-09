package com.cuecolab.cuecolab.backend.service.interfaces;

import com.cuecolab.cuecolab.backend.DTOs.videoDTOs.EntryDTOs.VideoUploadToDestinationRequestDTO;
import com.cuecolab.cuecolab.backend.DTOs.videoDTOs.ResponseDTOs.VideoInfo;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface VideoService {

    String playVideo(String videoId) throws Exception;

    String downloadVideo(String videoIdString) throws Exception;

    String deleteVideo(String videoIdString);

    String lockVideo(String videoIdString);

    String unlockVideo(String videoIdString);

    String uploadVideoToDestination(VideoUploadToDestinationRequestDTO videoUploadToDestinationRequestDTO) throws JsonProcessingException;

    String saveInfo(String videoId, VideoInfo videoInfo);

    VideoInfo getInfo(String videoId);
}
