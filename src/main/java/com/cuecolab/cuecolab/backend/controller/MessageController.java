package com.cuecolab.cuecolab.backend.controller;

import com.cuecolab.cuecolab.backend.DTOs.messageDTOs.entryDTOs.MessageEntryDTO;
import com.cuecolab.cuecolab.backend.service.MessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageServiceImpl messageService;

    @PostMapping
    public ResponseEntity<String> saveMessage(@RequestBody MessageEntryDTO messageEntryDTO) {
        return ResponseEntity.ok(messageService.saveMessage(messageEntryDTO));
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable UUID messageId) {
        return ResponseEntity.ok(messageService.deleteMessage(messageId));
    }
}
