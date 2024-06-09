package com.cuecolab.cuecolab.backend.service;

import com.cuecolab.cuecolab.backend.DTOs.roomDTOs.entryDTOs.RoomEntryDTO;
import com.cuecolab.cuecolab.backend.DTOs.roomDTOs.responseDTOs.RoomInvitesSummaryDTO;
import com.cuecolab.cuecolab.backend.DTOs.roomDTOs.responseDTOs.RoomResponseDTO;
import com.cuecolab.cuecolab.backend.DTOs.roomDTOs.responseDTOs.RoomSummaryDTO;
import com.cuecolab.cuecolab.backend.DTOs.roomparticipantDTOs.responseDTOs.RoomParticipantSummaryDTO;
import com.cuecolab.cuecolab.backend.convertors.Convertors;
import com.cuecolab.cuecolab.backend.entities.*;
import com.cuecolab.cuecolab.backend.enums.ChatName;
import com.cuecolab.cuecolab.backend.enums.ChatType;
import com.cuecolab.cuecolab.backend.enums.Role;
import com.cuecolab.cuecolab.backend.exceptions.NoUserFoundInTheDatabaseException;
import com.cuecolab.cuecolab.backend.exceptions.PublicChatEntityNotFoundException;
import com.cuecolab.cuecolab.backend.repository.RoomParticipantRepository;
import com.cuecolab.cuecolab.backend.repository.RoomRepository;
import com.cuecolab.cuecolab.backend.repository.UserRepository;
import com.cuecolab.cuecolab.backend.service.eventService.room_events_service.RoomEventsServiceImpl;
import com.cuecolab.cuecolab.backend.service.eventService.user_account_events_service.UserAccountEventsServiceImpl;
import com.cuecolab.cuecolab.backend.service.interfaces.RoomService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(RoomServiceImpl.class);
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RoomParticipantRepository roomParticipantRepository;

    @Autowired
    Convertors convertors;

    @Autowired
    MessageSource messageSource;

    @Autowired
    UserAccountEventsServiceImpl userAccountEventsServiceImpl;

    @Autowired
    RoomEventsServiceImpl roomEventsServiceImpl;

    private static final Logger logger = Logger.getLogger(RoomServiceImpl.class.getName());


    //All these methods required custom security checks for authorisation
    //It has to be comprehensively checked that whether the user how is making the request
    //has the right permissions and authority to do so. I will implement custom authorisation
    //logic in each and every method here to ensure that the user has the right permissions.
    //But right now I am not implementing those things and implementing just core function logic for now
    //At the time of setup of authentication via spring security, custom authorisation will be implemented
    //here /*
    //        Now we will map each of this roomEntity (in this roomEntityList) to a RoomResponseDTO
    //        to send a summarised data to the frontend so that frontend can be able to show the first look of the room
    //        to the user. If user clicks further on any of the resource then again the request will come to the frontend
    //        with the UUID of that resource. Backend will again fetch the data using that UUID, and again the same principle
    //        of minimum data required to show the first of that resource
    //        */

    @Override
    @Transactional
    public List<RoomSummaryDTO> getAllRooms(UUID userId) { //Get
        //Authorization will be implemented later on

        //Now get all rooms which are mapped with this userId
        List<RoomEntity> roomEntityList = roomRepository.findAllByUserEntity_UserId(userId);
        return convertors.convertRoomEntityListToRoomSummaryDTOList(roomEntityList);
    }


    @Override
    @Transactional
    public RoomResponseDTO getRoom(String roomIdString) throws Exception { //Get
        //Authorization will be implemented later on
        UUID userId = CurrentUserId.getUserId();
        //Convert String to UUID
        UUID roomId = UUID.fromString(roomIdString);

        //Fetch the roomEntity from the database for the given roomId
        RoomEntity roomEntity = roomRepository.findByRoomId(roomId);
        return convertors.convertRoomEntityToRoomResponseDTO(roomEntity, userId);
    }

    @Override
    @Transactional
    public RoomSummaryDTO createRoom(RoomEntryDTO roomEntryDTO) { //Post
        //Authorization will be implemented later on


        //Retrieve the userId from roomEntryDTO and
        //fetch the userEntity for that userId from the database
        //so that this new room can be mapped with the user
        UUID userId = roomEntryDTO.getUserId();
        UserEntity userEntity = userRepository.findByUserId(userId);

        /*
        * DESCRIPTION: Now before we build the roomEntity we have to keep something very
        * important in our mind that: During creation of a new room, a
        * new public chat will also be created which will have
        * the creator (this owner user) as its member. Now because the
        * room has been created just know hence there will be no other members
        * but as soon as new members are added to the room they will automatically
        * become part of this public chat
        *
        * SUMMARY: Creating a new room also creates a public chat, initially for the creator,
        * which new members join automatically.
        * */

        /*
        * DESCRIPTION: If you see chatEntity, for every chat there is always a list
        * of members related to that chat who are called ChatParticipantsEntities,
        * hence we will have to create a list of ChatParticipantsEntityList, but what?
        * there are no ChatParticipantEntit"ies" yet, because right now there is only
        * one participant who is the owner of the room - means this user, so we will
        * create a new ChatParticipantEntity, put it into a newly created
        * ChatParticipantEntityList, and will add this list to the ChatEntity
        *
        * SUMMARY: Each chat entity includes a list of members
        * (ChatParticipantsEntities), starting with a single
        * participant—the room's owner—added to the chat upon creation.
        * */

        /*
        * create a new room participant entity corresponding to this user entity
        * create a new chat member entity corresponding to this user entity
        * create a new chat entity corresponding to this room entity
        * */

        RoomParticipantEntity roomParticipantEntity = new RoomParticipantEntity();
        ChatMemberEntity chatMemberEntity =  new ChatMemberEntity();
        ChatEntity ChatEntity = new ChatEntity();
        RoomEntity roomEntity = new RoomEntity();

        //Now setting the attributes of UserEntity
        userEntity.getRoomEntityList().add(roomEntity);
        userEntity.getRoomParticipantEntityList().add(roomParticipantEntity);

        //Now setting the attributes of RoomEntity
        roomEntity.setRoomName(roomEntryDTO.getRoomName());
        roomEntity.setUserEntity(userEntity);
        roomEntity.getChats().add(ChatEntity);
        roomEntity.getRoomParticipants().add(roomParticipantEntity);


        //Now setting the attributes of chatEntity
        ChatEntity.setChatName(ChatName.PUBLIC_CHAT.toString());
        ChatEntity.getChatMemberEntityList().add(chatMemberEntity);
        ChatEntity.setRoomEntity(roomEntity);
        ChatEntity.setChatType(ChatType.GROUP_CHAT);



        //Now setting the attributes of chatMemberEntity
        chatMemberEntity.setRoomParticipantEntity(roomParticipantEntity);
        chatMemberEntity.setChatEntity(ChatEntity);



        //Now setting the attributes of RoomParticipantEntity
        roomParticipantEntity.setRole(Role.CREATOR);
        roomParticipantEntity.setUserEntity(userEntity);
        roomParticipantEntity.setRoomEntity(roomEntity);
        roomParticipantEntity.getChatMemberEntityList().add(chatMemberEntity);

        RoomEntity roomEntityFromDB = roomRepository.save(roomEntity);

        return RoomSummaryDTO.builder()
                .roomId(roomEntityFromDB.getRoomId())
                .roomName(roomEntityFromDB.getRoomName())
                .build();
    }

    @Override
    @Transactional
    public String deleteRoom(String roomIdString) { //Delete
        //Authorization will be implemented later on

        UUID roomIdToBeDeleted = UUID.fromString(roomIdString);

        RoomEntity roomEntity = roomRepository.findByRoomId(roomIdToBeDeleted);

        //================================================================
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        //================================================================
        /*
         * As the room is getting removed from the database, hence all the
         * videos of this room will also get removed, therefore, the
         * ----------------------
         * memory == total size of all the videos of this room
         * ----------------------
         * needs to be freed for this user's account
         * */
        //================================================================
        int totalMemoryToBeFreed = getTotalMemoryToBeFreed(roomEntity);
        UUID userId = CurrentUserId.getUserId();
        UserEntity userEntity = userRepository.findByUserId(userId);
        int storageUsed = userEntity.getStorageUsed() - totalMemoryToBeFreed;
        userEntity.setStorageUsed(storageUsed);
        userRepository.save(userEntity);

        //We will send event to frontend regarding update
        userAccountEventsServiceImpl.sentEvent_StorageUpdate(userId, storageUsed);

        //Find out all the roomParticipant_UserId who are participants in this room
        //and send this event to all of them
        List <RoomParticipantEntity> roomParticipantEntityList = roomEntity.getRoomParticipants();
        List<UUID> roomParticipant_UserId_List = new ArrayList<UUID>();
        for(RoomParticipantEntity roomParticipantEntity : roomParticipantEntityList){
            roomParticipant_UserId_List.add(roomParticipantEntity.getUserEntity().getUserId());
        }
        for(UUID roomParticipant_UserId : roomParticipant_UserId_List){
            userAccountEventsServiceImpl.sendEvent_RoomInviteRevoked(roomParticipant_UserId, roomIdToBeDeleted);
        }

        //================================================================
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        //================================================================

        roomRepository.deleteByRoomId(roomIdToBeDeleted);

        return messageSource.getMessage(
                "room.deletion.message",
                //Arguments
                new Object[]{

                },
                LocaleContextHolder.getLocale()
        );
    }

    private static int getTotalMemoryToBeFreed(RoomEntity roomEntity) {
        List<VideoEntity> videoEntityList = roomEntity.getVideos();
        int totalMemoryToBeFreed = 0;
        for(VideoEntity videoEntity : videoEntityList){
            totalMemoryToBeFreed += videoEntity.getVideoFileSize();
        }
        return totalMemoryToBeFreed;
    }

    @Override
    @Transactional
    public String addRoomParticipant(String roomIdString, String email) { //Post
        //Authorization will be implemented later on

        ////////////////////////////////////////////////////////////////////////////
        /*First we will fetch the userId corresponding to this email and this will happen if and only if
        *the account corresponding to given email exists. If it does not exist then we will send a message and an
        * exception that account does not exist
        *
        * And also we will fetch the roomEntity corresponding to this roomId
        * */
        ////////////////////////////////////////////////////////////////////////////

        UUID roomId = UUID.fromString(roomIdString);

        UserEntity userEntityToBeAddedInThisRoom = userRepository.findByEmail(email);
        if(userEntityToBeAddedInThisRoom == null) {
            throw new NoUserFoundInTheDatabaseException();
        }
        RoomEntity roomEntity = roomRepository.findByRoomId(roomId);

        //========================================================
        //We will set the attributes of roomParticipantEntity here
        //========================================================
        RoomParticipantEntity roomParticipantEntity = new RoomParticipantEntity();
        roomParticipantEntity.setRole(Role.EDITOR);
        RoomParticipantEntity savedRoomParticipantEntity = roomParticipantRepository.save(roomParticipantEntity);

        ////////////////////////////////////////////////////////////////////////////
        //Setting up parent - child relationship between
        // 1-this roomEntity and this new roomParticipantEntity
        // AND
        // 2-userEntity (who is going to be a participant in this room) and this new roomParticipantEntity
        ////////////////////////////////////////////////////////////////////////////
            //========================================================
            //We will map this roomParticipantEntity with its parents
            //========================================================
            savedRoomParticipantEntity.setUserEntity(userEntityToBeAddedInThisRoom);
            savedRoomParticipantEntity.setRoomEntity(roomEntity);

            //========================================================
            //We will also map those parents with their child roomParticipantEntity
            //========================================================
            userEntityToBeAddedInThisRoom.getRoomParticipantEntityList().add(savedRoomParticipantEntity);
            roomEntity.getRoomParticipants().add(savedRoomParticipantEntity);



        ////////////////////////////////////////////////////////////////////////////
        //Since here a new roomParticipantEntity is being created who will also be a default member of
        //the public chat of this room, so here we will create a new
        //chatMemberEntity corresponding to this roomParticipantEntity and will 1-add it to the
        //chatMemberEntityList of this roomParticipantEntity and 2-will also add it to the
        //chatMemberEntityList of the public chat of this room
        ////////////////////////////////////////////////////////////////////////////

        ChatMemberEntity chatMemberEntity = new ChatMemberEntity();

        ////////////////////////////////////////////////////////////////////////////
        //Setting up parent - child relationship between this new chatMemberEntity
        //and this new roomParticipantEntity
        ////////////////////////////////////////////////////////////////////////////
            //========================================================
            //Adding this chatMemberEntity to the chatMemberEntityList of the roomParticipantEntity
            //========================================================
            savedRoomParticipantEntity.getChatMemberEntityList().add(chatMemberEntity);

            //========================================================
            //Setting roomParticipantEntity as a parent of this new chatMemberEntity
            //========================================================
            chatMemberEntity.setRoomParticipantEntity(savedRoomParticipantEntity);

        //========================================================
        //Establishing parent-child relationship between this new
        //ChatMemberEntity and the public chatEntity of this room
        //========================================================
            List<ChatEntity> chatEntityList = roomEntity.getChats();
            Optional<ChatEntity> optionalChatEntityOfThisRoom = chatEntityList.stream()
                    .filter(member -> member.getChatName().equals(ChatName.PUBLIC_CHAT.toString()))
                    .findFirst();

            if(optionalChatEntityOfThisRoom.isPresent()){
                ChatEntity chatEntityOfThisRoom = optionalChatEntityOfThisRoom.get();

                //parent-child relationship setup
                chatEntityOfThisRoom.getChatMemberEntityList().add(chatMemberEntity);
                chatMemberEntity.setChatEntity(chatEntityOfThisRoom);
            }
            //although it will not happen that public chat does not exist, but still we need to add
            //exception for this rare possibility
            else {
                throw new PublicChatEntityNotFoundException("Public chat of this room does not exist");
            }

        //Now we will save the roomEntity only and chatMemberEntity will be saved automatically
        //due to cascading effect
        roomRepository.save(roomEntity);

        UUID newRoomParticipantId = savedRoomParticipantEntity.getRoomParticipantId();
        String newRoomParticipantEmail = savedRoomParticipantEntity.getUserEntity().getEmail();


        /*
        * 1. Send an event to roomParticipant's user account regarding the update (addition) of
        * Room Invites List
        * 2. Send an event to all rooms which are opened right now for updating (addition) the
        * list of editors (roomParticipants) in the room
        * */

        //======================
        //Implementing number 1:
        //======================
        RoomSummaryDTO roomSummaryDTO = RoomSummaryDTO.builder()
                .roomId(roomId)
                .roomName(roomEntity.getRoomName())
                .build();
        String hostEmail = roomEntity.getUserEntity().getEmail();
        RoomInvitesSummaryDTO roomInvitesSummaryDTO = RoomInvitesSummaryDTO.builder()
                .roomSummaryDTO(roomSummaryDTO)
                .hostEmail(hostEmail)
                .build();

        userAccountEventsServiceImpl.sendEvent_NewRoomInvite(userEntityToBeAddedInThisRoom.getUserId(),
                roomInvitesSummaryDTO);


        //======================
        //Implementing number 2:
        //======================
        RoomParticipantSummaryDTO roomParticipantSummaryDTO = RoomParticipantSummaryDTO.builder()
                .roomParticipantId(newRoomParticipantId)
                .roomParticipantEmail(newRoomParticipantEmail)
                .build();

        roomEventsServiceImpl.sendEvent_NewEditorAdded(roomId, roomParticipantSummaryDTO);

        return "Editor Added Successfully";
    }

    @Override
    @Transactional
    public String deleteRoomParticipant(UUID roomParticipantId) { //Delete
        //Authorization will be implemented later on


        RoomParticipantEntity roomParticipantEntity = roomParticipantRepository.findByRoomParticipantId(roomParticipantId);
        UUID roomParticipant_UserId = roomParticipantEntity.getUserEntity().getUserId();
        UUID roomId = roomParticipantEntity.getRoomEntity().getRoomId();


        roomParticipantRepository.deleteByRoomParticipantId(roomParticipantId);

        /*
         * 1. Send an event to roomParticipant's user account regarding the update (deletion) of
         * Room Invites List
         * 2. Send an event to all rooms which are opened right now for updating (deletion) the
         * list of editors (roomParticipants) in the room
         * */

        //======================
        //Implementing number 1:
        //======================
        userAccountEventsServiceImpl.sendEvent_RoomInviteRevoked(roomParticipant_UserId, roomId);


        //======================
        //Implementing number 2:
        //======================
        roomEventsServiceImpl.sendEvent_EditorRemoved(roomId, roomParticipantId);

        return "Editor Removed Successfully";
    }

    //This API will actually be used by the user that in what rooms is he invited, because
    //each of the roomParticipantEntity belongs to one and only one room, hence if he clicks
    //on the "room invites" button to see what are those rooms, where he got invited into
    //then in that case he will call this api to fetch all the roomParticipantEntities which
    //are child of his userEntity, and from those roomParticipantEntity it will become easy to
    //retrieve which roomEntity that roomParticipantEntity belongs to

    @Override
    @Transactional
    public List<RoomInvitesSummaryDTO> getAllRoomsUserIsInvitedTo(UUID userId) {
        //================================================================
        //We will convert the userIdString into UUID

        //================================================================
        //We will fetch the userEntity for this userId
        UserEntity userEntity = userRepository.findByUserId(userId);

        //We will retrieve the roomParticipantList of this userEntity
        List<RoomParticipantEntity> roomParticipantEntityList = userEntity.getRoomParticipantEntityList();

        //================================================================
        //A user is also a participant in his own created room. We will exclude all those rooms in the final
        //response of which user is the owner
        //In other words, we have to include only those rooms in the final response in which user is just
        //a participant
        //================================================================
        //So, here is the list of roomEntities which are to be excluded from the final list
        //as user is not only a participant but also an owner (creator) of these rooms
        List<RoomEntity> roomsOwnedByUser = userEntity.getRoomEntityList();
        Set<UUID> roomIdOfUserOwnedRooms = roomsOwnedByUser.stream()
                .map(RoomEntity::getRoomId)
                .collect(Collectors.toSet());

        //Now we will create the final list
        List<RoomEntity> roomsInWhichUserIsAParticipant = roomParticipantEntityList.stream()
                .map(RoomParticipantEntity::getRoomEntity)
                .filter(roomEntity -> roomEntity != null && !roomIdOfUserOwnedRooms.contains(roomEntity.getRoomId()))
                .collect(Collectors.toList());
        return convertors.convertRoomEntityListToRoomInvitesSummaryDTOList(roomsInWhichUserIsAParticipant);
    }

    //================================================================
    //================================================================

}
