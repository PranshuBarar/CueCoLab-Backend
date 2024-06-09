package com.cuecolab.cuecolab.backend.service.eventService.room_events_service;

import com.cuecolab.cuecolab.backend.DTOs.roomDTOs.responseDTOs.RoomInvitesSummaryDTO;
import com.cuecolab.cuecolab.backend.DTOs.roomparticipantDTOs.responseDTOs.RoomParticipantSummaryDTO;
import com.cuecolab.cuecolab.backend.DTOs.videoDTOs.ResponseDTOs.VideoSummaryDTO;
import com.cuecolab.cuecolab.backend.convertors.Convertors;
import com.cuecolab.cuecolab.backend.entities.UserEntity;
import com.cuecolab.cuecolab.backend.entities.VideoEntity;
import com.cuecolab.cuecolab.backend.repository.UserRepository;
import com.cuecolab.cuecolab.backend.service.eventService.user_account_events_service.UserAccountEventsServiceImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

@Service
public class RoomEventsServiceImpl {

    @Autowired
    Convertors convertors;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserAccountEventsServiceImpl userAccountEventsServiceImpl;


    private static final Logger logger = Logger.getLogger(RoomEventsServiceImpl.class.getName());

    //This will contain the list of subscriptions to a particular room. roomId will be key here
    //and the list of SseEmitter will be the values corresponding to each roomId
    private final Map<UUID, List<SseEmitter>> emittersForRoomLevelEvents = new ConcurrentHashMap<>();

    //================================================================
    //////////////////////////////////////////////////////////////////
    //================================================================
    //Add emitter for Room level events
    public SseEmitter addEmitterForRoomLevelEvents(String roomIdString, SseEmitter sseEmitter){
        UUID roomId = UUID.fromString(roomIdString);
        List<SseEmitter> roomEmitters = emittersForRoomLevelEvents.computeIfAbsent(roomId, mappingFunction -> new CopyOnWriteArrayList<>());
        roomEmitters.add(sseEmitter);
        logger.info("Emitter added successfully");

        //Remove emitter when it is completed or times out
        sseEmitter.onCompletion(() -> roomEmitters.remove(sseEmitter));
        sseEmitter.onTimeout(() -> roomEmitters.remove(sseEmitter));
        logger.info(sseEmitter.toString());
        return sseEmitter;
    }





    //================================================================
    //////////////////////////////////////////////////////////////////
    //================================================================
    //Send Room level events

    //---------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------
    /*
    * This section sends all video related events
    * */

    @Async
    @Transactional
    public void sendEvent_VideoListUpdate(UUID roomId, VideoEntity videoEntity, Boolean afterProcessing, UUID roomOwner_UserId)
            throws Exception {
        VideoSummaryDTO videoSummaryDTO;
        if(afterProcessing){
            videoSummaryDTO = convertors.convertVideoEntityToVideoSummaryDTOIfThumbnailNotNull(videoEntity);

            //If after processing then an storage update event will be sent to fronted
            UserEntity userEntity = userRepository.findByUserId(roomOwner_UserId);
            userAccountEventsServiceImpl.sentEvent_StorageUpdate(roomOwner_UserId, userEntity.getStorageUsed());
        } else {
            videoSummaryDTO = convertors.convertVideoEntityToVideoSummaryDTOIfThumbnailNull(videoEntity);
        }
        List<SseEmitter> roomEmitters = emittersForRoomLevelEvents.getOrDefault(roomId, new ArrayList<>());
        for (SseEmitter emitter : roomEmitters) {
            try {
                logger.info("Sending emitter");
                emitter.send(SseEmitter.event().name("VideoListUpdateEvent").data(videoSummaryDTO));
            } catch (IOException e) {
                logger.info("Not able to send emitter due to error" + e.getMessage());
                emitter.completeWithError(e);
            }
        }
    }

    @Async
    public void sendEvent_VideoDelete(UUID roomId, UUID videoId){
        List<SseEmitter> roomEmitters = emittersForRoomLevelEvents.getOrDefault(roomId, new ArrayList<>());
        for(SseEmitter emitter : roomEmitters){
            try{
                logger.info("Sending Emitter");
                emitter.send(SseEmitter.event().name("VideoDeleteEvent").data(videoId));
            } catch (IOException e) {
                logger.info("Not able to send emitter due to error" + e.getMessage());
                emitter.completeWithError(e);
            }
        }
    }

    @Async
    public void sendEvent_VideoLocked(UUID roomId, UUID videoId){
        List<SseEmitter> roomEmitters = emittersForRoomLevelEvents.getOrDefault(roomId, new ArrayList<>());
        for(SseEmitter emitter : roomEmitters){
            try{
                logger.info("Sending Emitter");
                emitter.send(SseEmitter.event().name("VideoLockedEvent").data(videoId));
            } catch (IOException e) {
                logger.info("Not able to send emitter due to error" + e.getMessage());
                emitter.completeWithError(e);
            }
        }
    }

    @Async
    public void sendEvent_VideoUnlocked(UUID roomId, UUID videoId){
        List<SseEmitter> roomEmitters = emittersForRoomLevelEvents.getOrDefault(roomId, new ArrayList<>());
        for(SseEmitter emitter : roomEmitters){
            try{
                logger.info("Sending Emitter");
                emitter.send(SseEmitter.event().name("VideoUnlockedEvent").data(videoId));
            } catch (IOException e) {
                logger.info("Not able to send emitter due to error" + e.getMessage());
                emitter.completeWithError(e);
            }
        }
    }

    //---------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------
    /*
     * This section sends all editor related events
     * */

    @Async
    public void sendEvent_NewEditorAdded(UUID roomId, RoomParticipantSummaryDTO roomParticipantSummaryDTO){
        List<SseEmitter> roomEmitters = emittersForRoomLevelEvents.getOrDefault(roomId, new ArrayList<>());
        for(SseEmitter emitter : roomEmitters){
            try{
                logger.info("Sending Emitter");
                emitter.send(SseEmitter.event().name("NewEditorAddedEvent").data(roomParticipantSummaryDTO));
            } catch (IOException e) {
                logger.info("Not able to send emitter due to error " + e.getMessage());
                emitter.completeWithError(e);
            }
        }
    }

    @Async
    public void sendEvent_EditorRemoved(UUID roomId, UUID roomParticipantId){
        List<SseEmitter> roomEmitters = emittersForRoomLevelEvents.getOrDefault(roomId, new ArrayList<>());
        for(SseEmitter emitter : roomEmitters){
            try{
                logger.info("Sending Emitter");
                emitter.send(SseEmitter.event().name("EditorRemovedEvent").data(roomParticipantId));
            } catch (IOException e) {
                logger.info("Not able to send emitter due to error" + e.getMessage());
                emitter.completeWithError(e);
            }
        }
    }
}
