package com.cuecolab.cuecolab.backend.service;

import com.cuecolab.cuecolab.backend.DTOs.chatDTOs.responseDTOs.ChatDetailsDTO;
import com.cuecolab.cuecolab.backend.convertors.Convertors;
import com.cuecolab.cuecolab.backend.entities.ChatEntity;
import com.cuecolab.cuecolab.backend.entities.ChatMemberEntity;
import com.cuecolab.cuecolab.backend.entities.RoomEntity;
import com.cuecolab.cuecolab.backend.entities.RoomParticipantEntity;
import com.cuecolab.cuecolab.backend.enums.ChatType;
import com.cuecolab.cuecolab.backend.repository.ChatRepository;
import com.cuecolab.cuecolab.backend.repository.RoomParticipantRepository;
import com.cuecolab.cuecolab.backend.repository.RoomRepository;
import com.cuecolab.cuecolab.backend.service.interfaces.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private RoomParticipantRepository roomParticipantRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private Convertors convertors;

    @Autowired
    private MessageSource messageSource;


    @Override
    @Transactional
    public ChatDetailsDTO getChatDetails(UUID chatId) {
        //Authorization will be implemented later on

        //First we will fetch chatEntity for the corresponding chatId from the
        //database
        ChatEntity chatEntity = chatRepository.findByChatId(chatId);

        //Then we will convert this chatEntity into chatDetailsDTO and return it
        return convertors.convertChatEntityToChatDetailsDTO(chatEntity);
    }

    @Override
    @Transactional
    public String createChat(List<UUID> roomParticipantIds, UUID roomId, String chatName) {
        //Authorization will be implemented later on

        //----------------------------------------------------------------
        ChatEntity chatEntity =  new ChatEntity(); //This we have to return after saving it
        //in the database
        //----------------------------------------------------------------



        //----------------------------------------------------------------
        //We will initialize an empty roomParticipantEntityList
        List<RoomParticipantEntity> roomParticipantEntityList = new ArrayList<>();

        //We will fill the roomParticipantEntityList with all the roomParticipantEntities corresponding
        //to the given roomParticipantIds
        for(UUID roomParticipantId : roomParticipantIds){
            RoomParticipantEntity roomParticipantEntity = roomParticipantRepository.findByRoomParticipantId(roomParticipantId);
            roomParticipantEntityList.add(roomParticipantEntity);
        }
        //----------------------------------------------------------------



        //----------------------------------------------------------------
        //We will initialize an empty chatMemberEntityList
        List<ChatMemberEntity> chatMemberEntityList = new ArrayList<>();

        //We will fill the chatMemberEntityList with all the newly created chatMemberEntities
        //corresponding to the above fetched roomParticipantsEntities
        for(RoomParticipantEntity roomParticipantEntity : roomParticipantEntityList){
            //----------------------------------------------------------------
            //Initialize a new chatMemberEntity
            ChatMemberEntity chatMemberEntity =  new ChatMemberEntity();
            //----------------------------------------------------------------

            //----------------------------------------------------------------
            //Set the above initialized chatEntity as the parent of this chatMemberEntity
            chatMemberEntity.setChatEntity(chatEntity);
            //----------------------------------------------------------------

            //----------------------------------------------------------------
            //Establish parent-child relationship
            chatMemberEntity.setRoomParticipantEntity(roomParticipantEntity);
            roomParticipantEntity.getChatMemberEntityList().add(chatMemberEntity);
            //----------------------------------------------------------------

            //----------------------------------------------------------------
            //Finally add this chatMemberEntity to the above initialized chatMemberEntityList
            chatMemberEntityList.add(chatMemberEntity);
            //----------------------------------------------------------------
        }

        //----------------------------------------------------------------
        //Set the above newly created chatMemberEntityList as the child of the
        //above initialized chatEntity
        chatEntity.setChatMemberEntityList(chatMemberEntityList);
        //----------------------------------------------------------------

        chatEntity.setChatName(chatName);
        chatEntity.setChatType(ChatType.GROUP_CHAT);


        //----------------------------------------------------------------
        //Set the above newly created chatEntity as the child of the roomEntity corresponding
        //to the given roomId and also that roomEntity as parent of this chatEntity
        RoomEntity roomEntity = roomRepository.findByRoomId(roomId);

        chatEntity.setRoomEntity(roomEntity);
        ChatEntity savedChatEntity = chatRepository.save(chatEntity);

        roomEntity.getChats().add(savedChatEntity);
        roomRepository.save(roomEntity);

        return messageSource.getMessage(
                "chat.creation.success",
                new Object[]{
                        savedChatEntity.getChatId(),
                        savedChatEntity.getChatName()
                },
                LocaleContextHolder.getLocale()
        );
    }

    @Override
    @Transactional
    public String DeleteChat(UUID chatId) {
        //Authorization will be implemented later on

        chatRepository.deleteByChatId(chatId);
        return messageSource.getMessage(
                "chat.deletion.success",
                new Object[]{
                        chatId
                },
                LocaleContextHolder.getLocale()
        );

    }
}
