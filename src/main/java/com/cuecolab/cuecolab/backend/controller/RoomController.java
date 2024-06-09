package com.cuecolab.cuecolab.backend.controller;

import com.cuecolab.cuecolab.backend.DTOs.roomDTOs.entryDTOs.RoomEntryDTO;
import com.cuecolab.cuecolab.backend.DTOs.roomDTOs.responseDTOs.RoomInvitesSummaryDTO;
import com.cuecolab.cuecolab.backend.DTOs.roomDTOs.responseDTOs.RoomResponseDTO;
import com.cuecolab.cuecolab.backend.DTOs.roomDTOs.responseDTOs.RoomSummaryDTO;
import com.cuecolab.cuecolab.backend.DTOs.videoDTOs.ResponseDTOs.VideoSummaryDTO;
import com.cuecolab.cuecolab.backend.securityConfig.otpbasedloginandsignup.CustomUserDetails;
import com.cuecolab.cuecolab.backend.exceptions.NoUserFoundInTheDatabaseException;
import com.cuecolab.cuecolab.backend.service.eventService.room_events_service.RoomEventsServiceImpl;
import com.cuecolab.cuecolab.backend.service.RoomServiceImpl;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomEventsServiceImpl roomEventsServiceImpl;

    @Autowired
    private RoomServiceImpl roomService;

    @RateLimiter(name = "getAllRooms_RateLimiter")
    @GetMapping
    public ResponseEntity<List<RoomSummaryDTO>> getAllRooms() {
        UUID userId = getUserId();
        return ResponseEntity.ok(roomService.getAllRooms(userId));
    }

    @RateLimiter(name = "getRoom_RateLimiter")
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponseDTO> getRoom(@PathVariable("roomId") String roomIdString) throws Exception {
        RoomResponseDTO roomResponseDTO = roomService.getRoom(roomIdString);
        return ResponseEntity.ok(roomResponseDTO);
    }

    @RateLimiter(name = "createRoom_RateLimiter")
    @PostMapping("/create_room")
    public ResponseEntity<RoomSummaryDTO> createRoom(@RequestBody String roomName) {
        RoomEntryDTO roomEntryDTO = RoomEntryDTO.builder()
                .roomName(roomName)
                .userId(getUserId())
                .build();
        return ResponseEntity.ok(roomService.createRoom(roomEntryDTO));
    }

    @RateLimiter(name = "deleteRoom_RateLimiter")
    @DeleteMapping("/delete_room/{roomId}")
    public ResponseEntity<String> deleteRoom(@PathVariable("roomId") String roomIdString) {
        return ResponseEntity.ok(roomService.deleteRoom(roomIdString));
    }

    @RateLimiter(name = "addRoomParticipant_RateLimiter")
    @PostMapping("/{roomId}/add_room_participant")
    public ResponseEntity<?> addRoomParticipant(@PathVariable("roomId") String roomIdString, @RequestBody String email) {
        try {
            String response = roomService.addRoomParticipant(roomIdString, email);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NoUserFoundInTheDatabaseException e) {
            return new ResponseEntity<>("User with this email is not registered on CueCoLab", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RateLimiter(name = "deleteRoomParticipant_RateLimiter")
    @DeleteMapping("/delete_participant/{participantId}")
    public ResponseEntity<String> deleteRoomParticipant(@PathVariable UUID participantId) {
        return ResponseEntity.ok(roomService.deleteRoomParticipant(participantId));
    }

    @RateLimiter(name = "getAllRoomsUserIsInvitedTo_RateLimiter")
    @GetMapping("/get_invited_rooms")
    public ResponseEntity<?> getAllRoomsUserIsInvitedTo() {
        try {
            List<RoomInvitesSummaryDTO> roomInvitesSummaryDTOList = roomService.getAllRoomsUserIsInvitedTo(getUserId());
            return ResponseEntity.ok(roomInvitesSummaryDTOList);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
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