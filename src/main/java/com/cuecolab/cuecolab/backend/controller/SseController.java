package com.cuecolab.cuecolab.backend.controller;

import com.cuecolab.cuecolab.backend.securityConfig.otpbasedloginandsignup.CustomUserDetails;
import com.cuecolab.cuecolab.backend.service.eventService.room_events_service.RoomEventsServiceImpl;
import com.cuecolab.cuecolab.backend.service.eventService.user_account_events_service.UserAccountEventsServiceImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping
public class SseController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SseController.class);
    @Autowired
    RoomEventsServiceImpl roomEventsServiceImpl;

    @Autowired
    UserAccountEventsServiceImpl userAccountEventsServiceImpl;

    private static final Logger logger = Logger.getLogger(SseController.class.getName());

    //================================================================
    //////////////////////////////////////////////////////////////////
    //================================================================
    //Room level event subscriptions
    @CrossOrigin
    @GetMapping("/subscribe/room_level_events/{currentRoomId}")
    public SseEmitter subscribe_room_level_events(@PathVariable("currentRoomId") String roomIdString){
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        return roomEventsServiceImpl.addEmitterForRoomLevelEvents(roomIdString, emitter);
    }

    //================================================================
    //////////////////////////////////////////////////////////////////
    //================================================================
    //User Account level event subscriptions
    @CrossOrigin
    @GetMapping("subscribe/user_account_level_events")
    public SseEmitter subscribeUserAccountLevelEvents(){
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        UUID userId = getUserId();
        return userAccountEventsServiceImpl.addEmitterForUserAccountLevelEvents(userId, emitter);
    }

    //================================================================
    //Private method only available to the functions of this class
    //================================================================
    private UUID getUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            return UUID.fromString(customUserDetails.getUsername());
        }
        return null;
    }
}
