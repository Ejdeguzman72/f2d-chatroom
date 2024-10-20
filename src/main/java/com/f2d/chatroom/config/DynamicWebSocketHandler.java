package com.f2d.chatroom.config;

import com.f2d.chatroom.domain.ChatGroup;
import com.f2d.chatroom.domain.ChatGroupListResponse;
import com.f2d.chatroom.domain.ChatMessageAddUpdateRequest;
import com.f2d.chatroom.service.F2DChatGroupService;
import com.f2d.chatroom.service.F2DChatMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class DynamicWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(DynamicWebSocketHandler.class);

    @Autowired
    private F2DChatGroupService chatGroupService;

    @Autowired
    private static F2DChatMessageService chatMessageService;

    public static Map<String, WebSocketHandler> channelHandlers = new ConcurrentHashMap<>();

    public Set<ChatGroup> retrieveChatGroupNames() {
        ChatGroupListResponse list;
        try {
            list = chatGroupService.retrieveAllChatGroupInfo();
            for (ChatGroup x : list.getList()) {
                logger.info(x.toString());
            }
        } catch (Exception e) {
            logger.error("Failed to retrieve chat groups: {}", e.getMessage());
            return new HashSet<>();
        }

        if (list == null || list.getList() == null) {
            return new HashSet<>();
        }

        return new HashSet<>(list.getList());
    }

    @PostConstruct
    public void initializeHandlers() {
        Set<ChatGroup> set = retrieveChatGroupNames();
        for (ChatGroup cg : set) {
            logger.info("Chat Group: " + cg);
            channelHandlers.put(cg.getGroupName(), new F2DWebSocketHandler(cg.getGroupName()));
        }
    }

    // Return the handler for the requested channel
    public WebSocketHandler getHandlerForChannel(String channel) {
        return channelHandlers.getOrDefault(channel, new F2DWebSocketHandler("default"));
    }

    // Return available channels
    public Set<String> getChannels() {
        return channelHandlers.keySet();
    }

    public static class F2DWebSocketHandler extends TextWebSocketHandler {
        private String channel;

        public F2DWebSocketHandler(String channel) {
            this.channel = channel;
        }

//        @Override
//        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//            URI uri = session.getUri();
//            this.channel = getChannelNameFromQueryParams(uri);
//            logger.info("Connection established on {}: {}", channel, session.getId());
//        }

        /**
         * Method to get channel name from query parameters or dynamically choose from available channels.
         */
//        private String getChannelNameFromQueryParams(URI uri) {
//            String query = uri.getQuery(); // e.g., channelName=myChannel
//            if (query == null || query.isEmpty()) {
//                logger.error("No query parameters found in the WebSocket URI");
//                return getFirstAvailableChannel(); // Fallback to first available channel
//            }
//
//            Map<String, String> queryParams = Arrays.stream(query.split("&"))
//                    .map(param -> param.split("="))
//                    .filter(paramArr -> paramArr.length == 2) // Ensure valid key-value pairs
//                    .collect(Collectors.toMap(p -> p[0], p -> p[1]));
//
//            String channelName = queryParams.get("channelName");
//
//            // If channelName is provided and valid, return it
//            if (channelName != null && channelHandlers.containsKey(channelName)) {
//                return channelName;
//            } else {
//                logger.error("Invalid or missing channelName in query parameters");
//                return getFirstAvailableChannel(); // Fallback to first available channel
//            }
//        }
//
//        // Helper method to get the first available channel from the channelHandlers map
//        private String getFirstAvailableChannel() {
//            if (!channelHandlers.isEmpty()) {
//                return channelHandlers.keySet().iterator().next(); // Return the first available channel
//            } else {
//                logger.error("No available channels found");
//                return "noChannel"; // Return an indication if no channels exist
//            }
//        }

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            logger.info("Message received on {}: {}", channel, message.getPayload());
            session.sendMessage(new TextMessage("Channel " + channel + ": " + message.getPayload()));
            try {
                ChatMessageAddUpdateRequest request = new ChatMessageAddUpdateRequest();
                String content = message.getPayload();
                String sender = "server";  // Update this according to your logic
                request.setSender(sender);
                request.setContent(content);
                request.setChatGroupId(UUID.fromString("a92139eb-7604-4e33-bdd8-48cbefbd138d")); // Set proper ChatGroup ID

                // Save chat message
                chatMessageService.createChatMessage(request);
                logger.info("Message saved on channel: {}", channel);
            } catch (Exception e) {
                logger.error("Error saving message on channel {}: {}", channel, e.getMessage());
            }
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
            logger.info("Connection closed on {}: {}", channel, session.getId());
        }
    }
}