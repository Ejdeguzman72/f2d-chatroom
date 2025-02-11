package com.f2d.chatroom.config;

import com.f2d.chatroom.domain.ChatGroup;
import com.f2d.chatroom.domain.ChatGroupListResponse;
import com.f2d.chatroom.service.F2DChatGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);
    private final DynamicWebSocketHandler dynamicWebSocketHandler;
    @Autowired
    private F2DChatGroupService chatGroupService;

    // Constructor injection of DynamicWebSocketHandler
    public WebSocketConfig(DynamicWebSocketHandler dynamicWebSocketHandler) {
        this.dynamicWebSocketHandler = dynamicWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // Registering the default channel handler
        registry.addHandler(new DynamicWebSocketHandler.F2DWebSocketHandler("default"), "/ws/default")
                .setAllowedOrigins("*");

        // Retrieve chat groups and register dynamic handlers
        try {
            ChatGroupListResponse chatGroupListResponse = chatGroupService.retrieveAllChatGroupInfo();
            if (chatGroupListResponse != null && chatGroupListResponse.getList() != null) {
                for (ChatGroup chatGroup : chatGroupListResponse.getList()) {
                    registry.addHandler(new DynamicWebSocketHandler.F2DWebSocketHandler(chatGroup.getGroupName()),
                                    "/ws/" + chatGroup.getGroupName())
                            .setAllowedOrigins("*");
                }
            } else {
                logger.error("No chat groups found or list is null");
            }
        } catch (Exception e) {
            logger.error("Failed to retrieve chat groups: {}", e.getMessage());
        }
    }

    // Dynamically register all available channels
//        dynamicWebSocketHandler.getChannels().forEach(channel -> {
//            registry.addHandler(new DynamicWebSocketHandler.F2DWebSocketHandler(channel), "/ws/" + channel)
//                    .setAllowedOrigins("*");
//        });
}
