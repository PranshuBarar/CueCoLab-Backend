package com.cuecolab.cuecolab.backend.service.interfaces;


import com.cuecolab.cuecolab.backend.DTOs.roomDTOs.entryDTOs.RoomEntryDTO;
import com.cuecolab.cuecolab.backend.DTOs.roomDTOs.responseDTOs.RoomInvitesSummaryDTO;
import com.cuecolab.cuecolab.backend.DTOs.roomDTOs.responseDTOs.RoomResponseDTO;
import com.cuecolab.cuecolab.backend.DTOs.roomDTOs.responseDTOs.RoomSummaryDTO;
import com.cuecolab.cuecolab.backend.DTOs.roomparticipantDTOs.responseDTOs.RoomParticipantSummaryDTO;

import java.util.List;
import java.util.UUID;

public interface RoomService {
    List<RoomSummaryDTO> getAllRooms(UUID userId); //Get

    RoomResponseDTO getRoom(String roomIdString) throws Exception; //Get

    RoomSummaryDTO createRoom(RoomEntryDTO roomEntryDTO); //Post

    String deleteRoom(String roomIdString); //Should be MFA based or OTP based //Delete

    String addRoomParticipant(String roomIdString, String email);   // Here the UUID of the user should
                                                            // be fetched using given email
                                                            // from the database and then
                                                            // using that UUID the user should
                                                            // be created in the RoomParticipant
                                                            // database

                                                            // Put

    String deleteRoomParticipant(UUID roomParticipantId); //Delete

    //Now this will actually give all the roomParticipantEntities which belong to a particular
    //user - means all those roomParticipantEntities to whom this user is the parent
    //Now from those roomParticipantEntities we can get the ids of all those RoomEntities
    //and then we can fetch those RoomEntities. Now further all the things which this
    //Room contains will come out of the database
    //Now because the user requested the rooms data as a roomParticipantEntity and not a
    //userEntity, hence only particular things will be shown to the user, like something will be
    //omitted like
    // 1-no video locking/un-locking capability
    // 2-no "video upload to channel" option
    // 3-no deletion of the locked videos
    // 4-no deletion of the videos which are unlocked BUT he/she has not uploaded those videos
    // and somebody else has uploaded it
    // 5-no room deletion capability
    // 6-no "new editor (user) addition/removal to the room" capability

    List<RoomInvitesSummaryDTO> getAllRoomsUserIsInvitedTo(UUID userId);
}
