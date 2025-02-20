package com.f2d.chatroom.controller;

import com.f2d.chatroom.domain.ChatMessage;
import com.f2d.chatroom.domain.ChatMessageAddUpdateRequest;
import com.f2d.chatroom.domain.ChatMessageAddUpdateResponse;
import com.f2d.chatroom.service.F2DChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class ChatController {

    @Autowired
    private F2DChatMessageService chatMessageService;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/public")
    public ChatMessageAddUpdateResponse send(@Payload ChatMessageAddUpdateRequest chatMessage) {
        ChatMessageAddUpdateResponse response = chatMessageService.createChatMessage(chatMessage);

        return response;
    }
}
