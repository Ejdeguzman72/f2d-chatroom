package com.f2d.chatroom.config;

import com.f2d.chatroom.repository.F2DChatGroupRepository;
import com.f2d.chatroom.repository.F2DChatMessageRepository;
import com.f2d.chatroom.service.F2DChatGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final F2DChatMessageRepository chatMessageRepository;
    private final F2DChatGroupRepository chatGroupRepository;
    private final F2DChatGroupService chatGroupService;

    @Autowired
    public WebSocketConfig(
            F2DChatMessageRepository chatMessageRepository,
            F2DChatGroupRepository chatGroupRepository,
            F2DChatGroupService chatGroupService
    ) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatGroupRepository = chatGroupRepository;
        this.chatGroupService = chatGroupService;
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatWebSocketHandler(chatMessageRepository, chatGroupRepository, chatGroupService), "/f2d-chat").setAllowedOrigins("*");
    }
}
