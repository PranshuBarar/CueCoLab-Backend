package com.cuecolab.cuecolab.backend.service.eventService.user_account_events_service;

import com.cuecolab.cuecolab.backend.DTOs.roomDTOs.responseDTOs.RoomInvitesSummaryDTO;
import com.cuecolab.cuecolab.backend.DTOs.videoDTOs.ResponseDTOs.VideoSummaryDTO;
import com.cuecolab.cuecolab.backend.convertors.Convertors;
import com.cuecolab.cuecolab.backend.entities.VideoEntity;
import com.cuecolab.cuecolab.backend.service.eventService.room_events_service.RoomEventsServiceImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
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
public class UserAccountEventsServiceImpl {

    @Autowired
    Convertors convertors;


    private static final Logger logger = Logger.getLogger(UserAccountEventsServiceImpl.class.getName());


    //Send User Accounts level events
    private final Map<UUID, List<SseEmitter>> emittersForUserAccountLevelEvents = new ConcurrentHashMap<>();

    //================================================================
    //////////////////////////////////////////////////////////////////
    //================================================================
    //Add emitter for User Account level events
    public SseEmitter addEmitterForUserAccountLevelEvents(UUID userId, SseEmitter sseEmitter) {
        List<SseEmitter> userAccountEmitters = emittersForUserAccountLevelEvents.computeIfAbsent(userId, mappingFunction -> new CopyOnWriteArrayList<>());
        userAccountEmitters.add(sseEmitter);
        logger.info("Emitter added successfully");

        //Remove emitter when it is completed or times out
        sseEmitter.onCompletion(() -> userAccountEmitters.remove(sseEmitter));
        sseEmitter.onTimeout(() -> userAccountEmitters.remove(sseEmitter));
        logger.info(sseEmitter.toString());

        return sseEmitter;
    }
    //================================================================
    //////////////////////////////////////////////////////////////////
    //================================================================


    @Async
    public void sendEvent_NewRoomInvite(UUID userIdOfTheUserWhoGotInvited, RoomInvitesSummaryDTO roomInvitesSummaryDTO){
        List<SseEmitter> userAccountLevelEmitters = emittersForUserAccountLevelEvents.getOrDefault(userIdOfTheUserWhoGotInvited, new ArrayList<>());
        for(SseEmitter emitter : userAccountLevelEmitters){
            try{
                logger.info("Sending Emitter");
                emitter.send(SseEmitter.event().name("NewRoomInviteAddedEvent").data(roomInvitesSummaryDTO));
            } catch (IOException e) {
                logger.info("Not able to send emitter due to error" + e.getMessage());
                emitter.completeWithError(e);
            }
        }
    }

    @Async
    public void sendEvent_RoomInviteRevoked(UUID userId, UUID roomId){
        List<SseEmitter> userAccountLevelEmitters = emittersForUserAccountLevelEvents.getOrDefault(userId, new ArrayList<>());
        for(SseEmitter emitter : userAccountLevelEmitters){
            try{
                logger.info("Sending Emitter");
                emitter.send(SseEmitter.event().name("RoomInviteRevokedEvent").data(roomId));
            } catch (IOException e) {
                logger.info("Not able to send emitter due to error" + e.getMessage());
                emitter.completeWithError(e);
            }
        }
    }

    @Async
    public void sentEvent_StorageUpdate(UUID userId, double storageUsed){
        List<SseEmitter> userAccountLevelEmitters = emittersForUserAccountLevelEvents.getOrDefault(userId, new ArrayList<>());
        for(SseEmitter emitter : userAccountLevelEmitters){
            try{
                logger.info("Sending Emitter");
                emitter.send(SseEmitter.event().name("StorageUpdateEvent").data(storageUsed));
            } catch (IOException e) {
                logger.info("Not able to send emitter due to error" + e.getMessage());
                emitter.completeWithError(e);
            }
        }
    }
}
