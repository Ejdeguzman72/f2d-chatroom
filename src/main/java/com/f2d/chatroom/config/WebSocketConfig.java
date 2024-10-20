package com.f2d.chatroom.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final DynamicWebSocketHandler dynamicWebSocketHandler;

    // Constructor injection of DynamicWebSocketHandler
    public WebSocketConfig(DynamicWebSocketHandler dynamicWebSocketHandler) {
        this.dynamicWebSocketHandler = dynamicWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // Registering the default channel handler
        registry.addHandler(new DynamicWebSocketHandler.F2DWebSocketHandler("default"), "/ws/default")
                .setAllowedOrigins("*");

        // Dynamically register all available channels
        dynamicWebSocketHandler.getChannels().forEach(channel -> {
            registry.addHandler(new DynamicWebSocketHandler.F2DWebSocketHandler(channel), "/ws/" + channel)
                    .setAllowedOrigins("*");
        });
    }
}
