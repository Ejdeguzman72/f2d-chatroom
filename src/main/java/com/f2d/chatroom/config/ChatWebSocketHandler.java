package com.f2d.chatroom.config;

import com.f2d.chatroom.domain.ChatGroup;
import com.f2d.chatroom.domain.ChatMessage;
import com.f2d.chatroom.domain.F2DGroupSearchResponse;
import com.f2d.chatroom.feign.F2DGroupBuilderFeignClient;
import com.f2d.chatroom.repository.F2DChatGroupRepository;
import com.f2d.chatroom.repository.F2DChatMessageRepository;
import com.f2d.chatroom.service.F2DChatGroupService;
import com.f2d.chatroom.service.F2DChatMessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

public class ChatWebSocketHandler extends TextWebSocketHandler {
    private static final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private F2DChatMessageRepository chatMessageRepository;
    @Autowired
    private F2DChatGroupService chatGroupService;
    @Autowired
    private F2DChatMessageService chatMessageService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatWebSocketHandler.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        System.out.println("New user connected: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
        System.out.println("Received message: " + message.getPayload());
        ChatGroup chatGroup = chatGroupService.retrieveChatGroupById(UUID.fromString("fc51b749-d510-46b5-8188-8e37f1ff4825")).getChatGroup();
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(message.getPayload());
        chatMessage.setSentDatetime(LocalDateTime.now());
        chatMessage.setChatGroup(chatGroup);

        chatMessageRepository.save(chatMessage);
        LOGGER.info("Saving Message to database: " + chatMessage.toString());

        // Broadcast the message to all connected clients
        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                try {
                    s.sendMessage(new TextMessage(message.getPayload()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        sessions.remove(session);
        System.out.println("User disconnected: " + session.getId());
    }
}
