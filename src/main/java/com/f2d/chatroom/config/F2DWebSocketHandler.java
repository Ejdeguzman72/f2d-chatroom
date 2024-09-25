package com.f2d.chatroom.config;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class F2DWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connection established: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Message received: " + payload);

        // Echo the message back to the client
        session.sendMessage(new TextMessage("F2D Server response: " + payload));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        System.out.println("Connection closed: " + session.getId());
    }
}