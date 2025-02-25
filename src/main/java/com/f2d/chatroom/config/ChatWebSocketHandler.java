package com.f2d.chatroom.config;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;

public class ChatWebSocketHandler extends TextWebSocketHandler {
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("WebSocket connection established: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Parse the incoming JSON message to extract username and message
        String messageContent = message.getPayload();
        System.out.println("Received: " + messageContent);

        // You may want to use a library to parse JSON, such as Jackson or Gson
        // For simplicity, assuming the message is in the format: {"username": "user", "message": "text"}

        // Echo the message with the username
        session.sendMessage(new TextMessage(messageContent));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("WebSocket connection closed: " + session.getId());
    }
}
