package com.cuecolab.cuecolab.backend.service.interfaces;

import com.cuecolab.cuecolab.backend.DTOs.messageDTOs.entryDTOs.MessageEntryDTO;

import java.util.UUID;

public interface MessageService {
    String saveMessage(MessageEntryDTO messageEntryDTO);

    String deleteMessage(UUID messageId);
}
