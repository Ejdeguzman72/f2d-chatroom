package com.f2d.chatroom.repository;

import com.f2d.chatroom.domain.ChatGroup;
import com.f2d.chatroom.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface F2DChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    List<ChatMessage> findChatMessagesByChatGroup(ChatGroup chatGroup);
}
