package com.cuecolab.cuecolab.backend.repository;

import com.cuecolab.cuecolab.backend.entities.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, UUID> {

    ChatEntity findByChatId(UUID chatId);

    void deleteByChatId(UUID chatId);
}
