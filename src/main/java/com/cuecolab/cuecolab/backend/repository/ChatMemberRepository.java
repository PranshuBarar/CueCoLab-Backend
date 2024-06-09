package com.cuecolab.cuecolab.backend.repository;

import com.cuecolab.cuecolab.backend.entities.ChatMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatMemberRepository extends JpaRepository<ChatMemberEntity, UUID> {
    ChatMemberEntity findByChatMemberId(UUID chatMemberId);
}
