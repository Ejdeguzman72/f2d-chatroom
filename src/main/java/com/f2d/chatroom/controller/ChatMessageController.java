package com.f2d.chatroom.controller;

import com.f2d.chatroom.domain.*;
import com.f2d.chatroom.repository.F2DChatMessageRepository;
import com.f2d.chatroom.service.F2DChatMessageService;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
public class ChatMessageController {

    @Autowired
    private F2DChatMessageService chatMessageService;

    @GetMapping(UriConstants.GET_ALL_CHAT_MESSAGES_URI)
    public ResponseEntity<ChatMessageListResponse> retrieveAllChatMessages() {
        ChatMessageListResponse response = chatMessageService.retrieveAllChatMessages();
        return ResponseEntity.ok(response);
    }

    @GetMapping(UriConstants.GET_ALL_CHAT_MESSAGES_BY_GROUP_URI)
    public ResponseEntity<ChatMessageListResponse> retrieveChatMessagesByGroup(@PathVariable UUID chatGroupId) {
        ChatMessageListResponse response = chatMessageService.retrieveChatMessagesByGroup(chatGroupId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(UriConstants.GET_CHAT_MESSAGE_BY_ID_URI)
    public ResponseEntity<ChatMessageSearchResponse> retrieveChatMessageById(@PathVariable long chatMessageId) {
        ChatMessageSearchResponse response = chatMessageService.retrieveChatMessageById(chatMessageId);
        return ResponseEntity.ok(response);
    }

    @PostMapping(UriConstants.CREATE_CHAT_MESSAGE_URI)
    public ResponseEntity<ChatMessageAddUpdateResponse> createChatMessage(@RequestBody ChatMessageAddUpdateRequest request) {
        ChatMessageAddUpdateResponse response = chatMessageService.createChatMessage(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping(UriConstants.UPDATE_CHAT_MESSAGE_URI)
    public ResponseEntity<ChatMessageAddUpdateResponse> updateChatMessage(@RequestBody ChatMessageAddUpdateRequest request, @PathVariable long chatMessageId) {
        ChatMessageAddUpdateResponse response = chatMessageService.updateChatMessage(request, chatMessageId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(UriConstants.DELETE_CHAT_MESSAGE_URI)
    public ResponseEntity<ChatMessageSearchResponse> deleteChatMessage(@PathVariable long chatMessageId) {
        ChatMessageSearchResponse response = chatMessageService.deleteChatMessage(chatMessageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(UriConstants.RETRIEVE_USERNAME_FROM_TOKEN_URI)
    public ResponseEntity<String> getUserInfo(@RequestHeader("X-User-Name") String username) {
        // Use the username for your logic
        return ResponseEntity.ok("Username: " + username);
    }
}
