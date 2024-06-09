package com.cuecolab.cuecolab.backend.convertors;

import com.cuecolab.cuecolab.backend.DTOs.chatDTOs.responseDTOs.ChatDetailsDTO;
import com.cuecolab.cuecolab.backend.DTOs.chatDTOs.responseDTOs.ChatSummaryDTO;
import com.cuecolab.cuecolab.backend.DTOs.chatmemberDTOs.responseDTOs.ChatMemberDTO;
import com.cuecolab.cuecolab.backend.DTOs.destinationDTOs.responseDTOs.DestinationDTO;
import com.cuecolab.cuecolab.backend.DTOs.messageDTOs.responseDTOs.MessageResponseDTO;
import com.cuecolab.cuecolab.backend.DTOs.roomDTOs.responseDTOs.RoomInvitesSummaryDTO;
import com.cuecolab.cuecolab.backend.DTOs.roomDTOs.responseDTOs.RoomResponseDTO;
import com.cuecolab.cuecolab.backend.DTOs.roomDTOs.responseDTOs.RoomSummaryDTO;
import com.cuecolab.cuecolab.backend.DTOs.roomparticipantDTOs.responseDTOs.RoomParticipantSummaryDTO;
import com.cuecolab.cuecolab.backend.DTOs.videoDTOs.ResponseDTOs.VideoInfo;
import com.cuecolab.cuecolab.backend.DTOs.videoDTOs.ResponseDTOs.VideoSummaryDTO;
import com.cuecolab.cuecolab.backend.aws.SignedURLGenerators;
import com.cuecolab.cuecolab.backend.entities.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class Convertors {
    @Autowired
    SignedURLGenerators signedURLGenerators;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Convertors.class);

    public List<DestinationDTO> convertDestinationEntityListToDestinationDTOList(List<DestinationEntity> destinationEntityList) {
        return destinationEntityList.stream()
                .map(this::convertDestinationEntityToDestinationDTO)
                .collect(Collectors.toList());
    }

    public DestinationDTO convertDestinationEntityToDestinationDTO(DestinationEntity destinationEntity) {
        return DestinationDTO.builder()
                .destinationId(destinationEntity.getDestinationId())
                .destinationType(destinationEntity.getDestinationType().toString())
                .destinationUserName(destinationEntity.getDestinationUserName())
                .build();
    }


    //=============================================================================================
    //////////////////Conversion of RoomEntityList to RoomResponseDTOList\\\\\\\\\\\\\\\\\\\\\\\\\\
    //=============================================================================================
//    public List<RoomResponseDTO> convertRoomEntityListToRoomResponseDTOList(List<RoomEntity> roomEntityList) {
//        return roomEntityList
//                .stream()
//                .map(this::convertRoomEntityToRoomResponseDTO)
//                .collect(Collectors.toList());
//    }

    public RoomResponseDTO convertRoomEntityToRoomResponseDTO(RoomEntity roomEntity, UUID userId) throws Exception {
        //First we will get all the three entities list from the provided roomEntity and convert them
        List<ChatEntity> allChatsList = roomEntity.getChats();
        List<VideoEntity> videoEntityList = roomEntity.getVideos();
        List<RoomParticipantEntity> roomParticipantEntityList = roomEntity.getRoomParticipants();

        //Now see, before doing anything further we will first filter all those chats in which
        //the user with the give userId is the member. We don't have to send any chat to the
        //frontend in which user (who called this api) is not the member

        //----------------------------------------------------------------
        //This is nested loop having time complexity of n^2
        //----------------------------------------------------------------
//        List<ChatEntity> filteredChatEntityList = new ArrayList<ChatEntity>();
//        for(ChatEntity chatEntity : allChatsList) {
//            List<ChatMemberEntity> chatMemberEntityList = chatEntity.getChatMemberEntityList();
//            for(ChatMemberEntity chatMemberEntity : chatMemberEntityList) {
//                if(chatMemberEntity.getRoomParticipantEntity().getUserEntity().getUserId().equals(userId)) {
//                    filteredChatEntityList.add(chatEntity);
//                    break;
//                }
//            }
//        }

        //----------------------------------------------------------------
        //This is code using stream api with time complexity of n
        //----------------------------------------------------------------
        //Create a set of chatIds where the user is the member
        Set<UUID> chatIdsWhereUserIsAMember = allChatsList.stream()
                .filter(chatEntity -> chatEntity.getChatMemberEntityList().stream()
                        .anyMatch(chatMemberEntity -> chatMemberEntity.getRoomParticipantEntity().getUserEntity().getUserId().equals(userId)))
                .map(ChatEntity::getChatId)
                .collect(Collectors.toSet());

        //Now filter the chats where the user is the member using the above chatIds set for quick look-up
        List<ChatEntity> filteredChatEntityList = allChatsList.stream()
                .filter(chatEntity -> chatIdsWhereUserIsAMember.contains(chatEntity.getChatId()))
                .collect(Collectors.toList());



        //And now we will convert them into their corresponding summaryDTOs for sending them to the frontend
        List<ChatSummaryDTO> chatSummaryDTOList = convertChatEntityListToChatSummaryDTOList(filteredChatEntityList);
        List<VideoSummaryDTO> videoSummaryDTOList = convertVideoEntityListToVideoSummaryDTOList(videoEntityList);
        List<RoomParticipantSummaryDTO> roomParticipantSummaryDTOList = convertRoomParticipantEntityListToRoomParticipantSummaryDTOList(roomParticipantEntityList, userId);

        return RoomResponseDTO.builder()
                .roomId(roomEntity.getRoomId())
                .roomName(roomEntity.getRoomName())
                .chats(chatSummaryDTOList)
                .videos(videoSummaryDTOList)
                .participants(roomParticipantSummaryDTOList)
                .build();
    }

    //=============================================================================================
    //////////////////Conversion of RoomParticipantEntityList to RoomParticipantSummaryDTOList\\\\\
    //=============================================================================================
    private List<RoomParticipantSummaryDTO> convertRoomParticipantEntityListToRoomParticipantSummaryDTOList(List<RoomParticipantEntity> roomParticipantEntityList, UUID userId) {

        return roomParticipantEntityList.stream()
                .filter(roomParticipantEntity -> !roomParticipantEntity.getUserEntity().getUserId().equals(userId))
                .map(this::convertRoomParticipantEntityToRoomParticipantSummaryDTO)
                .collect(Collectors.toList());
    }

    private RoomParticipantSummaryDTO convertRoomParticipantEntityToRoomParticipantSummaryDTO(RoomParticipantEntity roomParticipantEntity) {
        return RoomParticipantSummaryDTO.builder()
                .roomParticipantId(roomParticipantEntity.getRoomParticipantId())
                .roomParticipantEmail(roomParticipantEntity.getUserEntity().getEmail())
                .build();
    }

    //=============================================================================================
    //////////////////Conversion of VideoEntityList to VideoSummaryDTOList\\\\\\\\\\\\\\\\\\\\\\\\\
    //=============================================================================================



    private List<VideoSummaryDTO> convertVideoEntityListToVideoSummaryDTOList(List<VideoEntity> videoEntityList) throws  Exception {
        List<VideoSummaryDTO> videoSummaryDTOList = new ArrayList<>();
        for(VideoEntity videoEntity : videoEntityList){
            if(videoEntity.getVideoThumbnailS3URI() == null) {
                VideoSummaryDTO videoSummaryDTO = convertVideoEntityToVideoSummaryDTOIfThumbnailNull(videoEntity);
                videoSummaryDTOList.add(videoSummaryDTO);
            } else {
                VideoSummaryDTO videoSummaryDTO = convertVideoEntityToVideoSummaryDTOIfThumbnailNotNull(videoEntity);
                videoSummaryDTOList.add(videoSummaryDTO);
            }
        }
        return videoSummaryDTOList;
    }

    public VideoSummaryDTO convertVideoEntityToVideoSummaryDTOIfThumbnailNull(VideoEntity videoEntity) throws Exception {

        return VideoSummaryDTO.builder()
                .videoId(videoEntity.getVideoId())
                .videoFileName(videoEntity.getVideoFileName())
                .isLocked(videoEntity.isLocked())
                .uploadedBy(videoEntity.getUploadedBy())
                .build();
    }

    public VideoSummaryDTO convertVideoEntityToVideoSummaryDTOIfThumbnailNotNull(VideoEntity videoEntity) throws Exception {
        //We will convert plain videoThumbnailS3URI to cloudfront signed videoThumbnailS3URI

        String videoThumbnailS3URI = signedURLGenerators.createGetSignedUrlForCloudFrontForThumbnail(videoEntity.getVideoThumbnailS3URI());



        return VideoSummaryDTO.builder()
                .videoId(videoEntity.getVideoId())
                .videoThumbnailS3URI(videoThumbnailS3URI)
                .videoFileName(videoEntity.getVideoFileName())
                .isLocked(videoEntity.isLocked())
                .uploadedBy(videoEntity.getUploadedBy())
                .build();
    }

    //=============================================================================================
    //////////////////Conversion of ChatEntityList to ChatSummaryDTOList\\\\\\\\\\\\\\\\\\\\\\\\\
    //=============================================================================================
    private List<ChatSummaryDTO> convertChatEntityListToChatSummaryDTOList(List<ChatEntity> chatEntityList) {
        return chatEntityList.stream()
                .map(this::convertChatEntityToChatSummaryDTO)
                .collect(Collectors.toList());
    }

    private ChatSummaryDTO convertChatEntityToChatSummaryDTO(ChatEntity chatEntity) {
        return ChatSummaryDTO.builder()
                .chatId(chatEntity.getChatId())
                .chatName(chatEntity.getChatName())
                .build();
    }


    //=============================================================================================
    //////////////////Conversion of RoomEntityList to RoomSummaryDTOList\\\\\\\\\\\\
    //=============================================================================================

    public List<RoomSummaryDTO> convertRoomEntityListToRoomSummaryDTOList(List<RoomEntity> roomEntityList) {
        return roomEntityList.stream()
                .map(this::convertRoomEntityToRoomSummaryDTO)
                .collect(Collectors.toList());
    }

    private RoomSummaryDTO convertRoomEntityToRoomSummaryDTO(RoomEntity roomEntity) {
        return RoomSummaryDTO.builder()
                .roomId(roomEntity.getRoomId())
                .roomName(roomEntity.getRoomName())
                .build();
    }

    //=============================================================================================
    //////////////////Conversion of RoomParticipantEntityList to RoomSummaryDTOList\\\\\\\\\\\\
    //=============================================================================================

    public List<RoomSummaryDTO> 
    convertRoomParticipantEntityListIntoRoomSummaryDTOList
            (List<RoomParticipantEntity> roomParticipantEntityList) {

        return roomParticipantEntityList.stream()
                .map(this::convertRoomParticipantEntityToRoomSummaryDTO)
                .collect(Collectors.toList());
        
    }

    private RoomSummaryDTO convertRoomParticipantEntityToRoomSummaryDTO(RoomParticipantEntity roomParticipantEntity) {
        return RoomSummaryDTO.builder()
                .roomId(roomParticipantEntity.getRoomEntity().getRoomId())
                .roomName(roomParticipantEntity.getRoomEntity().getRoomName())
                .build();
    }

    //=============================================================================================
    //////////////////Conversion of ChatEntity to ChatDetailsDTO\\\\\\\\\\\\
    //=============================================================================================

    public ChatDetailsDTO convertChatEntityToChatDetailsDTO(ChatEntity chatEntity) {
        //First of all we will retrieve messageEntityList and chatMemberEntityList from the chatEntity
        List<MessageEntity> messageEntityList = chatEntity.getMessageEntityList();
        List<ChatMemberEntity> chatMemberEntityList = chatEntity.getChatMemberEntityList();

        List<MessageResponseDTO> messageResponseDTOList = convertMessageEntityListToMessageDTOLList(messageEntityList);
        List<ChatMemberDTO> chatMemberDTOList = convertChatMemberEntityListToChatMessageDTOList(chatMemberEntityList);

        return ChatDetailsDTO.builder()
                .chatMembers(chatMemberDTOList)
                .messages(messageResponseDTOList)
                .chatName(chatEntity.getChatName())
                .chatId(chatEntity.getChatId())
                .build();
    }

    //-------------------------------------------------------
    //Conversion of ChatMemberEntityList to ChatMemberDTOList
    //-------------------------------------------------------
    private List<ChatMemberDTO> convertChatMemberEntityListToChatMessageDTOList(List<ChatMemberEntity> chatMemberEntityList) {
        return chatMemberEntityList.stream()
                .map(this::convertChatMemberEntityToChatMemberDTO)
                .collect(Collectors.toList());
    }

    private ChatMemberDTO convertChatMemberEntityToChatMemberDTO(ChatMemberEntity chatMemberEntity) {
        return ChatMemberDTO.builder()
                .chatMemberId(chatMemberEntity.getChatMemberId())
                .chatMemberName(chatMemberEntity.getRoomParticipantEntity().getUserEntity().getEmail())
                .build();
    }

    //-------------------------------------------------------
    //Conversion of MessageEntityList to MessageDTOList
    //-------------------------------------------------------
    private List<MessageResponseDTO> convertMessageEntityListToMessageDTOLList(List<MessageEntity> messageEntityList) {
        return messageEntityList.stream()
                .map(this::convertMessageEntityToMessageDTO)
                .collect(Collectors.toList());
    }

    private MessageResponseDTO convertMessageEntityToMessageDTO(MessageEntity messageEntity) {
        return MessageResponseDTO.builder()
                .messageId(messageEntity.getMessageId())
                .senderName(messageEntity.getChatMemberEntity().getRoomParticipantEntity().getUserEntity().getEmail())
                .build();
    }


    public List<RoomInvitesSummaryDTO> convertRoomEntityListToRoomInvitesSummaryDTOList(List<RoomEntity> roomsInWhichUserIsAParticipant) {
        List<RoomInvitesSummaryDTO> roomInvitesSummaryDTOList = new ArrayList<>();
        for(RoomEntity roomEntity : roomsInWhichUserIsAParticipant){
            roomInvitesSummaryDTOList.add(convertRoomEntityToRoomInvitesSummaryDTO(roomEntity));
        }
        return roomInvitesSummaryDTOList;
    }

    private RoomInvitesSummaryDTO convertRoomEntityToRoomInvitesSummaryDTO(RoomEntity roomEntity) {
        RoomSummaryDTO roomSummaryDTO = RoomSummaryDTO.builder()
                .roomId(roomEntity.getRoomId())
                .roomName(roomEntity.getRoomName())
                .build();

        return RoomInvitesSummaryDTO.builder()
                .roomSummaryDTO(roomSummaryDTO)
                .hostEmail(roomEntity.getUserEntity().getEmail())
                .build();
    }


}
