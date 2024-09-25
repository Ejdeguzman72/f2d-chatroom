package com.f2d.chatroom.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class DynamicWebSocketHandler {

    // Map to store WebSocket handlers for different channels
    private final Map<String, WebSocketHandler> channelHandlers = new HashMap<>();

    public DynamicWebSocketHandler() {
        // Initialize dynamic handlers for channels
        channelHandlers.put("default", new MyWebSocketHandler("default"));
        // You can add more channels dynamically here
    }

    // Return the handler for the requested channel
    public WebSocketHandler getHandlerForChannel(String channel) {
        return channelHandlers.getOrDefault(channel, new MyWebSocketHandler("default"));
    }

    // Return available channels
    public Set<String> getChannels() {
        return channelHandlers.keySet();
    }

    // Custom WebSocket Handler for each channel
    public static class MyWebSocketHandler extends TextWebSocketHandler {
        private final String channel;

        public MyWebSocketHandler(String channel) {
            this.channel = channel;
        }

        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            System.out.println("Connection established on " + channel + ": " + session.getId());
        }

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            System.out.println("Message received on " + channel + ": " + message.getPayload());
            // Echo message back with channel info
            session.sendMessage(new TextMessage("Channel " + channel + ": " + message.getPayload()));
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
            System.out.println("Connection closed on " + channel + ": " + session.getId());
        }
    }
}
