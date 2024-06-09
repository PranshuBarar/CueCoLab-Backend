package com.cuecolab.cuecolab.backend.service;

import com.cuecolab.cuecolab.backend.DTOs.videoDTOs.EntryDTOs.VideoEntryDTO;
import com.cuecolab.cuecolab.backend.DTOs.videoDTOs.EntryDTOs.VideoUploadToDestinationRequestDTO;
import com.cuecolab.cuecolab.backend.DTOs.videoDTOs.ResponseDTOs.VideoInfo;
import com.cuecolab.cuecolab.backend.DTOs.videoDTOs.VideoUploadMessageToSQSDTO;
import com.cuecolab.cuecolab.backend.aws.SignedURLGenerators;
import com.cuecolab.cuecolab.backend.controller.VideoController;
import com.cuecolab.cuecolab.backend.entities.*;
import com.cuecolab.cuecolab.backend.repository.*;
import com.cuecolab.cuecolab.backend.service.eventService.room_events_service.RoomEventsServiceImpl;
import com.cuecolab.cuecolab.backend.service.eventService.user_account_events_service.UserAccountEventsServiceImpl;
import com.cuecolab.cuecolab.backend.service.interfaces.VideoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    RoomParticipantRepository roomParticipantRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    VideoRepository videoRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SignedURLGenerators signedURLGenerators;

    @Autowired
    RoomEventsServiceImpl roomEventsServiceImpl;

    @Autowired
    UserAccountEventsServiceImpl userAccountEventsServiceImpl;

    @Autowired
    DestinationRepository destinationRepository;


    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(VideoController.class);

    @Value("${CLOUDFRONT_DOMAIN_NAME}")
    String CLOUDFRONT_DOMAIN_NAME;

    @Value("${CLOUDFRONT_DOMAIN_NAME_FOR_RAW_VIDEO}")
    String CLOUDFRONT_DOMAIN_NAME_FOR_RAW_VIDEO;

    @Value("${CLOUDFRONT_KEY_PAIR_ID}")
    String CLOUDFRONT_KEY_PAIR_ID;

    @Value("${RAW_VIDEOS_BUCKET_NAME}")
    String BUCKET_NAME;

    @Value("${youtube-client-id}")
    private String YOUTUBE_CLIENT_ID;

    @Value("${youtube-client-secret}")
    private String YOUTUBE_CLIENT_SECRET;

    @Value("${video-upload-message-request-queue-url}")
    private String VIDEO_UPLOAD_MESSAGE_REQUEST_QUEUE_URL;

    @Autowired
    private ObjectMapper objectMapper;


    private final SqsClient sqsClient = SqsClient.builder().region(Region.AP_SOUTH_1).build();

    @Transactional
    public String[] uploadVideo(VideoEntryDTO videoEntryDTO) throws Exception {
        UUID uploader_UserId = UUID.fromString(videoEntryDTO.getUploader_UserId());
        UUID roomId = UUID.fromString(videoEntryDTO.getRoomId());
        RoomEntity roomEntity = roomRepository.findByRoomId(roomId);
        UUID roomOwner_UserId = roomEntity.getUserEntity().getUserId();


        //================================================================
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        //================================================================
        //Check whether this user account has limit left to upload the file?
        int fileSizeInMB = videoEntryDTO.getVideoFileSize();
        UserEntity userEntity = userRepository.findByUserId(roomOwner_UserId);
        int storageUsed = userEntity.getStorageUsed();
        int leftStorage = userEntity.getMaxStorage() - storageUsed;
        if(fileSizeInMB > 100 || fileSizeInMB > leftStorage) {
            throw new Exception("You do not have enough storage to upload this video file");
        }

        //If the execution flow passed above test it means still there is enough storage to upload the file
        int updatedStorage = storageUsed + fileSizeInMB;
        userEntity.setStorageUsed(updatedStorage);
        userRepository.save(userEntity);
        //================================================================
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        //================================================================

        RoomParticipantEntity roomParticipantEntity = roomParticipantRepository.findByRoomEntityRoomIdAndUserEntityUserId(roomId,uploader_UserId);

        //============================================================================
        //Here we need to save the video to the repository and also establish parent-child relationship
        //between videoEntity<->roomEntity and videoEntity<->roomParticipantEntity
        //============================================================================
        String videoUploaderEmailId = roomParticipantEntity.getUserEntity().getEmail();

        VideoEntity videoEntity = VideoEntity.builder()
                .signedURLRequestDateTime(LocalDateTime.now())
                .isLocked(false)
                .uploadedBy(videoUploaderEmailId)
                .videoFileName(videoEntryDTO.getVideoFileName())
                .roomEntity(roomEntity)
                .roomParticipantEntity(roomParticipantEntity)
                .videoFileSize(videoEntryDTO.getVideoFileSize())
                .build();

        VideoEntity savedVideoEntity = videoRepository.save(videoEntity);

        roomEntity.getVideos().add(savedVideoEntity);
        roomParticipantEntity.getVideoEntityList().add(savedVideoEntity);

        roomRepository.save(roomEntity);
        roomParticipantRepository.save(roomParticipantEntity);
        //============================================================================


        String videoFileName = videoEntryDTO.getVideoFileName();
        UUID videoId = savedVideoEntity.getVideoId();

        String keyName = roomOwner_UserId
                + "/"
                + roomId
                + "/"
                + videoId
                + "/"
                + videoFileName;

        String rawVideoS3URI = CLOUDFRONT_DOMAIN_NAME_FOR_RAW_VIDEO + "/" + keyName;
        savedVideoEntity.setRawVideoS3URI(rawVideoS3URI);

        //As we have set rawVideoS3URI, so again we will have to save the videoEntity in the database
        videoRepository.save(savedVideoEntity);
        roomEventsServiceImpl.sendEvent_VideoListUpdate(roomId, savedVideoEntity, false, roomOwner_UserId);
        String presignedUrl = signedURLGenerators.createPresignedPutUrl(BUCKET_NAME,keyName,fileSizeInMB,videoFileName);
        return new String[]{presignedUrl, String.valueOf(savedVideoEntity.getVideoId())};
    }

    @Transactional
    public String processedVideoDetailsUpdate(String videoIdString) throws Exception {
        UUID videoId = UUID.fromString(videoIdString);
        VideoEntity videoEntity = videoRepository.findById(videoId).orElseThrow();

        /*
        * We have to update actually three things
        * 1. videoThumbnailS3URI
        * 2. masterPlaylistS3URI
        * 3. processedVideoUploadDateTime
        *
        * and among these first two have to be built
        * */

        /*
        * Now what do we want for building these URIs?
        * 1. videoId = This we will get from lambda's call to this api
        * 2. roomId = This we will get from RoomEntity of which this VideoEntity
        * is child
        * 3. roomOwner_UserId = This we will get from RoomEntity's parent to whom
        * this RoomEntity belong
        * 4. videoFileName = This we will get from VideoEntity
        * 5. CLOUDFRONT_DOMAIN_NAME
        * */

        UUID roomOwner_UserId = videoEntity.getRoomEntity().getUserEntity().getUserId();
        UUID roomId = videoEntity.getRoomEntity().getRoomId();
        //VideoId we already have

        String commonPath = roomOwner_UserId + "/"
                + roomId
                + "/"
                + videoId
                + "/"
                + "processedMaterial"
                + "/"
                ;



        String videoThumbnailS3URI = commonPath + "thumbnail.jpg";
        String masterPlaylistS3URI = commonPath + "playlist.m3u8";
        LocalDateTime processedVideoUploadDateTime = LocalDateTime.now();

        videoEntity.setVideoThumbnailS3URI(videoThumbnailS3URI);
        videoEntity.setMasterPlaylistS3URI(masterPlaylistS3URI);
        videoEntity.setProcessedVideoUploadDateTime(processedVideoUploadDateTime);
        videoRepository.save(videoEntity);

        /*
        * Now here this spring-boot will emit a server side event to the frontend so that
        * the video list automatically gets updated in the room wherever that room is opened
        * */

        /*
        * What are the things to be sent to frontend as server-side-event?
        * 1. videoId
        * 2. videoFileName
        * 3. isLocked
        * 4. videoThumbnailS3URI
        //* 5. roomId
        * These are the things to be sent to frontend as server-side-event
        * */

        roomEventsServiceImpl.sendEvent_VideoListUpdate(roomId, videoEntity, true, roomOwner_UserId);
        return "Video details saved successfully";
    }

    @Override
    @Transactional
    public String playVideo(String videoIdString) throws Exception {
        UUID videoId = UUID.fromString(videoIdString);
        VideoEntity videoEntity = videoRepository.findById(videoId).orElseThrow();
        String masterPlaylistS3URI = videoEntity.getMasterPlaylistS3URI();
        String forSigning = masterPlaylistS3URI.substring(0, masterPlaylistS3URI.length() - "playlist.m3u8".length());
        String finalForSigning = forSigning + "*";
        String signedUrl = signedURLGenerators.createGetSignedUrlForCloudFrontForMasterPlaylist(finalForSigning);
        return modifyUrl(signedUrl);
    }

    @Override
    @Transactional
    public String downloadVideo(String videoIdString) throws Exception {
        UUID videoId = UUID.fromString(videoIdString);
        VideoEntity videoEntity = videoRepository.findById(videoId).orElseThrow();
        String rawVideoS3URI = videoEntity.getRawVideoS3URI();
        logger.info("This is rawVideoS3URI: " + rawVideoS3URI);
        return signedURLGenerators.createGetSignedUrlForCloudFrontForRawVideoDownload(rawVideoS3URI);
    }

    @Override
    @Transactional
    public String deleteVideo(String videoIdString) {
        UUID videoId = UUID.fromString(videoIdString);
        VideoEntity videoEntityToBeDeleted = videoRepository.findById(videoId).orElseThrow();
        int sizeOfVideo = videoEntityToBeDeleted.getVideoFileSize();
        UUID roomIdToWhichThisVideoBelongs = videoEntityToBeDeleted.getRoomEntity().getRoomId();
        RoomEntity roomEntity = roomRepository.findByRoomId(roomIdToWhichThisVideoBelongs);
        UUID roomOwner_UserId = roomEntity.getUserEntity().getUserId();
        videoRepository.deleteById(videoId);

        //=====================================================================
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        //=====================================================================

        //Here later on I will send a request to s3 to delete the said object

        //=====================================================================
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        //=====================================================================

        //================================================================
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        //================================================================
        /*
         * As the video is getting removed from the database, hence the total storage
         * used by this user account will be reduced back again
         * therefore, the
         * ----------------------
         * memory == size of this video
         * ----------------------
         * needs to be freed for this user's account
         * */
        //================================================================

        UserEntity userEntity = userRepository.findByUserId(roomOwner_UserId);
        int storageUsed = userEntity.getStorageUsed() - sizeOfVideo;
        userEntity.setStorageUsed(storageUsed);
        userRepository.save(userEntity);

        //We will send event to frontend regarding update
        userAccountEventsServiceImpl.sentEvent_StorageUpdate(roomOwner_UserId, storageUsed);
        //================================================================
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        //================================================================


        //We will also send SSE to frontend to notify it about the deletion of this video so that it should
        //remove the deleted video from the list and render the list again
        roomEventsServiceImpl.sendEvent_VideoDelete(roomIdToWhichThisVideoBelongs, videoId);
        return "Video deleted successfully";
    }

    @Override
    @Transactional
    public String lockVideo(String videoIdString) {
        //Security checks will be implemented later on
        UUID videoId = UUID.fromString(videoIdString);
        VideoEntity videoEntity = videoRepository.findById(videoId).orElseThrow();
        UUID roomId = videoEntity.getRoomEntity().getRoomId();
        videoEntity.setLocked(true);
        videoRepository.save(videoEntity);

        //We will send an event to all the event clients regarding the lock event
        roomEventsServiceImpl.sendEvent_VideoLocked(roomId, videoId);


        return "Video Locked Successfully";

    }

    @Override
    @Transactional
    public String unlockVideo(String videoIdString) {
        //Security checks will be implemented later on
        UUID videoId = UUID.fromString(videoIdString);
        VideoEntity videoEntity = videoRepository.findById(videoId).orElseThrow();
        videoEntity.setLocked(false);
        videoRepository.save(videoEntity);
        UUID roomId = videoEntity.getRoomEntity().getRoomId();
        //We will send an event to all the event clients regarding the unlock event
        roomEventsServiceImpl.sendEvent_VideoUnlocked(roomId, videoId);

        return "Video Unlocked Successfully";

    }

    @Override
    @Transactional
    public String uploadVideoToDestination(VideoUploadToDestinationRequestDTO videoUploadToDestinationRequestDTO) throws JsonProcessingException {
        UUID videoId = UUID.fromString(videoUploadToDestinationRequestDTO.getVideoId());
        UUID destinationId = UUID.fromString(videoUploadToDestinationRequestDTO.getDestinationId());

        VideoEntity videoEntity = videoRepository.findById(videoId).orElseThrow();
        DestinationEntity destinationEntity = destinationRepository.findById(destinationId).orElseThrow();
        String s3Path = getS3Path(videoEntity);
        VideoUploadMessageToSQSDTO message = new VideoUploadMessageToSQSDTO();
        message.setVideoId(videoId.toString());
        message.setDestinationId(destinationId.toString());
        message.setAccessToken(destinationEntity.getDestinationAccessToken());
        message.setClientId(YOUTUBE_CLIENT_ID);
        message.setClientSecret(YOUTUBE_CLIENT_SECRET);
        message.setS3Path(s3Path);
        message.setVideoTitle(videoEntity.getVideoTitle());
        message.setVideoDescription(videoEntity.getVideoDescription());
        message.setPrivacyStatus(videoUploadToDestinationRequestDTO.getPrivacyStatus());
        message.setTags(videoEntity.getVideoTags());
        message.setBucketName(BUCKET_NAME);

        String messageBody = convertObjectToJson(message);

        SendMessageRequest send_msg_request = SendMessageRequest.builder()
                .delaySeconds(0)
                .messageBody(messageBody)
                .queueUrl(VIDEO_UPLOAD_MESSAGE_REQUEST_QUEUE_URL)
                .build();

        sqsClient.sendMessage(send_msg_request);

        return "Message Sent to SQS for uploading video to youtube Successfully";
    }

    @Override
    public String saveInfo(String videoIdString, VideoInfo videoInfo) {
        UUID videoId = UUID.fromString(videoIdString);
        VideoEntity videoEntity = videoRepository.findById(videoId).orElseThrow();
        videoEntity.setVideoTitle(videoInfo.getTitle());
        videoEntity.setVideoDescription(videoInfo.getDescription());
        videoEntity.setVideoTags(videoInfo.getTags());
        videoRepository.save(videoEntity);
        return "Video Info saved successfully";
    }

    @Override
    public VideoInfo getInfo(String videoIdString) {
        UUID videoId = UUID.fromString(videoIdString);
        VideoEntity videoEntity = videoRepository.findById(videoId).orElseThrow();
        return VideoInfo.builder()
                .title(videoEntity.getVideoTitle())
                .description(videoEntity.getVideoDescription())
                .tags(videoEntity.getVideoTags())
                .build();
    }

    private static String getS3Path(VideoEntity videoEntity) {
        String s3Path = videoEntity.getRawVideoS3URI();
        int countOfSlashes = 0;
        for(int i=0; i<s3Path.length(); i++) {
            if(s3Path.charAt(i) == '/') {
                countOfSlashes++;
                if(countOfSlashes == 3){
                    s3Path = s3Path.substring(i+1);
                    break;
                }
            }
        }
        return s3Path;
    }

    private String convertObjectToJson(VideoUploadMessageToSQSDTO message) throws JsonProcessingException {
        return objectMapper.writeValueAsString(message);
    }

    //================================================================================================

    private String modifyUrl(String url) {
        return url.substring(0, url.lastIndexOf('*')) + "playlist.m3u8" + url.substring(url.indexOf('?'));
    }
}
