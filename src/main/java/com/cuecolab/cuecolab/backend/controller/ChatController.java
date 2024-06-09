package com.cuecolab.cuecolab.backend.controller;

import com.cuecolab.cuecolab.backend.DTOs.chatDTOs.responseDTOs.ChatDetailsDTO;
import com.cuecolab.cuecolab.backend.service.ChatServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/chats")
public class ChatController {

    @Autowired
    private ChatServiceImpl chatService;

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDetailsDTO> getChatDetails(@PathVariable UUID chatId) {
        return ResponseEntity.ok(chatService.getChatDetails(chatId));
    }

    @PostMapping
    public ResponseEntity<String> createChat(@RequestBody List<UUID> roomParticipantIds, @RequestParam UUID roomId, @RequestParam String chatName) {
        return ResponseEntity.ok(chatService.createChat(roomParticipantIds, roomId, chatName));
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<String> deleteChat(@PathVariable UUID chatId) {
        return ResponseEntity.ok(chatService.DeleteChat(chatId));
    }
}

