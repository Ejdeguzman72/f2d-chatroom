package com.f2d.chatroom.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final DynamicWebSocketHandler dynamicWebSocketHandler;

    public WebSocketConfig(DynamicWebSocketHandler dynamicWebSocketHandler) {
        this.dynamicWebSocketHandler = dynamicWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // Example for dynamic endpoints
        for (String channel : dynamicWebSocketHandler.getChannels()) {
            String endpoint = "/ws/" + channel;
            registry.addHandler(dynamicWebSocketHandler.getHandlerForChannel(channel), endpoint)
                    .setAllowedOrigins("*");
        }
    }
}
