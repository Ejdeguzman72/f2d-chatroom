package com.f2d.chatroom.controller;

import com.f2d.chatroom.domain.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage send(ChatMessage chatMessage) {
        return chatMessage;
    }
}
