package com.f2d.chatroom.config;

import com.f2d.chatroom.repository.F2DChatMessageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private F2DChatMessageRepository chatMessageRepository;

    private final Set<WebSocketSession> sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private static final String SECRET_KEY = "POOIRBCVIAUJERGKLBVSDLBVAKWIEWOIEOHGJKLBVLSBVLSADOWOIGHKLHGKLSDHJFKLSDFI"; // Replace with a secure key

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
//        String token = extractTokenFromUri(session);
//        if (token == null) {
//            System.out.println("No token found in WebSocket URI. Closing session.");
//            session.close();
//            return;
//        }
//
//        String username = extractUsernameFromToken(token);
//        if (username == null) {
//            System.out.println("Invalid token. Closing WebSocket session.");
//            session.close();
//            return;
//        }
//
//        System.out.println("User connected: " + username);
//        session.getAttributes().put("username", username);
//        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
        String username = (String) session.getAttributes().get("username");
        if (username == null) {
            System.out.println("No username associated with WebSocket session. Closing session.");
            closeSession(session);
            return;
        }

        String formattedMessage = "[" + username + "]: " + message.getPayload();
        System.out.println("Broadcasting message: " + formattedMessage);

        broadcastMessage(formattedMessage);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        System.out.println("User disconnected.");
    }

    private String extractTokenFromUri(WebSocketSession session) {
        URI uri = session.getUri();
        if (uri == null) return null;

        String query = uri.getQuery();
        if (query == null || !query.contains("token=")) return null;

        return query.split("token=")[1].split("&")[0]; // Extract token
    }

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    private String extractUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject(); // Username
        } catch (Exception e) {
            System.err.println("Token validation failed: " + e.getMessage());
            return null;
        }
    }

    private void broadcastMessage(String message) {
        synchronized (sessions) {
            for (WebSocketSession s : sessions) {
                if (s.isOpen()) {
                    try {
                        s.sendMessage(new TextMessage(message));
                    } catch (IOException e) {
                        System.err.println("Failed to send message: " + e.getMessage());
                    }
                }
            }
        }
    }

    private void closeSession(WebSocketSession session) {
        try {
            session.close();
        } catch (IOException e) {
            System.err.println("Error closing WebSocket session: " + e.getMessage());
        }
    }
}
