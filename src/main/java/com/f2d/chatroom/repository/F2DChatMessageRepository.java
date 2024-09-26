package com.f2d.chatroom.repository;

import com.f2d.chatroom.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface F2DChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findChatMessagesByChatGroup_ChatGroupId(UUID chatGroupId);  // Reference 'chatGroupId' instead of 'id'
}

