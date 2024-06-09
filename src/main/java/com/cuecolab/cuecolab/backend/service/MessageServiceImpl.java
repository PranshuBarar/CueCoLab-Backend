package com.cuecolab.cuecolab.backend.service;

import com.cuecolab.cuecolab.backend.DTOs.messageDTOs.entryDTOs.MessageEntryDTO;
import com.cuecolab.cuecolab.backend.entities.ChatEntity;
import com.cuecolab.cuecolab.backend.entities.ChatMemberEntity;
import com.cuecolab.cuecolab.backend.entities.MessageEntity;
import com.cuecolab.cuecolab.backend.repository.ChatMemberRepository;
import com.cuecolab.cuecolab.backend.repository.ChatRepository;
import com.cuecolab.cuecolab.backend.repository.MessageRepository;
import com.cuecolab.cuecolab.backend.service.interfaces.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private ChatRepository chatRepository;


    @Autowired
    private ChatMemberRepository chatMemberRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    @Transactional
    public String saveMessage(MessageEntryDTO messageEntryDTO) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setContent(messageEntryDTO.getMessageContent());
        messageEntity.setClientSideCreationDateTime(messageEntryDTO.getClientSideDateTime());
        messageEntity.setServerSideCreationDateTime(LocalDateTime.now());

        ChatEntity chatEntity = chatRepository.findByChatId(messageEntryDTO.getChatId());
        ChatMemberEntity chatMemberEntity = chatMemberRepository.findByChatMemberId(messageEntryDTO.getChatMemberId());

        messageEntity.setChatEntity(chatEntity);
        messageEntity.setChatMemberEntity(chatMemberEntity);

        MessageEntity savedMessageEntity = messageRepository.save(messageEntity);

        chatEntity.getMessageEntityList().add(savedMessageEntity);
        chatMemberEntity.getMessageEntityList().add(savedMessageEntity);

        chatRepository.save(chatEntity);
        chatMemberRepository.save(chatMemberEntity);

        return messageSource.getMessage(
                "message.saving.success",
                new Object[]{
                        savedMessageEntity.getMessageId(),
                        chatMemberEntity.getChatMemberId(),
                        chatEntity.getChatId()

                },
                LocaleContextHolder.getLocale()
        );
    }

    @Override
    @Transactional
    public String deleteMessage(UUID messageId) {
        messageRepository.deleteByMessageId(messageId);
        return messageSource.getMessage(
                "message.deletion.success",
                new Object[]{
                        messageId
                },
                LocaleContextHolder.getLocale()
        );
    }
}
