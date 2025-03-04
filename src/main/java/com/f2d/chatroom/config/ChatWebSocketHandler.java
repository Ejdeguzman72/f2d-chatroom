package com.f2d.chatroom.config;

import com.f2d.chatroom.repository.F2DChatMessageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.security.Key;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ChatWebSocketHandler extends TextWebSocketHandler {
    @Autowired
    private F2DChatMessageRepository chatMessageRepository;
    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
    private final String SECRET_KEY = "POOIRBCVIAUJERGKLBVSDLBVAKWIEWOIEOHGJKLBVLSBVLSADOWOIGHKLHGKLSDHJFKLSDFI"; // Change to your actual secret key

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        String token = extractTokenFromUri(session);
        if (token == null) {
            System.out.println("No token found in WebSocket URI.");
            session.close();
            return;
        }

        String username = extractUsernameFromToken(token);
        if (username == null) {
            System.out.println("Invalid token. Closing WebSocket session.");
            session.close();
            return;
        }

        System.out.println("User connected: " + username);
        session.getAttributes().put("username", username);
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
        String username = (String) session.getAttributes().get("username");
        if (username == null) {
            sessions.remove(session);
            System.out.println("No username associated with WebSocket session.");
            return;
        }

        String formattedMessage = "[" + username + "]: " + message.getPayload();
        System.out.println("Broadcasting message: " + formattedMessage);

        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                try {
                    s.sendMessage(new TextMessage(formattedMessage));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
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
            e.printStackTrace();
            return null;
        }
    }
}
