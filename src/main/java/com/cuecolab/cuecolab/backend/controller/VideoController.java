package com.cuecolab.cuecolab.backend.controller;

import com.cuecolab.cuecolab.backend.DTOs.videoDTOs.EntryDTOs.VideoEntryDTO;
import com.cuecolab.cuecolab.backend.DTOs.videoDTOs.EntryDTOs.VideoUpdateDetailsDTO;
import com.cuecolab.cuecolab.backend.DTOs.videoDTOs.EntryDTOs.VideoUploadToDestinationRequestDTO;
import com.cuecolab.cuecolab.backend.DTOs.videoDTOs.ResponseDTOs.VideoInfo;
import com.cuecolab.cuecolab.backend.service.VideoServiceImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/videos")
public class VideoController {

    @Autowired
    VideoServiceImpl videoServiceImpl;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(VideoController.class);

    @Value("${CLOUDFRONT_DOMAIN_NAME}")
    String CLOUDFRONT_DOMAIN_NAME;

    @Value("${CLOUDFRONT_KEY_PAIR_ID}")
    String CLOUDFRONT_KEY_PAIR_ID;

    @PutMapping("/upload")
    public ResponseEntity<?> uploadVideo(@RequestBody VideoEntryDTO videoEntryDTO) throws Exception {
        try {
            String[] responseArray = videoServiceImpl.uploadVideo(videoEntryDTO);
            return new ResponseEntity<>(responseArray, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to upload video", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/save_info/{videoId}")
    public ResponseEntity<?> saveInfo(@RequestBody VideoInfo videoInfo, @PathVariable("videoId") String videoId){
        try {
            String response = videoServiceImpl.saveInfo(videoId, videoInfo);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Failed to save info", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get_info/{videoId}")
    public ResponseEntity<?> getInfo(@PathVariable("videoId") String videoId){
        try {
            VideoInfo response = videoServiceImpl.getInfo(videoId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Failed to save info", HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/play/{videoId}")
    public ResponseEntity<?> playVideo(@PathVariable("videoId") String videoIdString){
        try {
            String cloudfrontSignedUrl = videoServiceImpl.playVideo(videoIdString);
            return new ResponseEntity<>(cloudfrontSignedUrl, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Video not found", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/download/{videoId}")
    public ResponseEntity<?> downloadVideo(@PathVariable("videoId") String videoIdString){
        try {
            String signedUrl = videoServiceImpl.downloadVideo(videoIdString);
            logger.info("Video download url: " + signedUrl);
            return new ResponseEntity<>(signedUrl, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Video not found", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{videoId}")
    public ResponseEntity<?> deleteVideo(@PathVariable("videoId") String videoIdString){
        try{
            String response = videoServiceImpl.deleteVideo(videoIdString);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Video not found", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update_video_details")
    public ResponseEntity<?> processedVideoDetailsUpdate(@RequestBody VideoUpdateDetailsDTO videoUpdateDetailsDTO){
        String videoId = videoUpdateDetailsDTO.getVideoId();
        try {
            String response = videoServiceImpl.processedVideoDetailsUpdate(videoId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Update not successful", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/lock/{videoId}")
    public ResponseEntity<?> lockVideo(@PathVariable("videoId") String videoIdString) {

        try {
            String response = videoServiceImpl.lockVideo(videoIdString);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("You are not authorized to lock this video as you are not owner of this room",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/unlock/{videoId}")
    public ResponseEntity<?> unlockVideo(@PathVariable("videoId") String videoIdString){
        try {
            String response = videoServiceImpl.unlockVideo(videoIdString);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("You are not authorized to lock this video as you are not owner of this room",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/upload_to_destination")
    public ResponseEntity<?> uploadVideoToDestination(@RequestBody VideoUploadToDestinationRequestDTO videoUploadToDestinationRequestDTO) {
        try {
            String response = videoServiceImpl.uploadVideoToDestination(videoUploadToDestinationRequestDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            String response = "Error uploading video";
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}