package com.f2d.chatroom.service;

import com.f2d.chatroom.domain.*;
import com.f2d.chatroom.feign.F2DGroupBuilderFeignClient;
import com.f2d.chatroom.repository.F2DChatGroupRepository;
import com.f2d.chatroom.repository.F2DChatMessageRepository;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class F2DChatMessageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(F2DChatMessageService.class);

    @Autowired
    private F2DChatMessageRepository chatMessageRepository;
    @Autowired
    private F2DChatGroupRepository chatGroupRepository;

    public ChatMessageListResponse retrieveAllChatMessages() {
        ChatMessageListResponse response = new ChatMessageListResponse();
        try {
            List<ChatMessage> list = chatMessageRepository.findAll();
            response.setList(list);
            response.setMessage(AppConstants.GET_ALL_CHAT_GROUPS_SUCCESS_MSG);
            response.setSuccess(true);
        } catch (Exception e) {
            response.setMessage(AppConstants.GET_ALL_CHAT_GROUPS_FAILURE_MSG);
            response.setSuccess(false);
        }

        return response;
    }

    public ChatMessageListResponse retrieveChatMessagesByGroup(UUID chatGroupId) {
        ChatMessageListResponse response = new ChatMessageListResponse();
        try {
            List<ChatMessage> list = chatMessageRepository.findChatMessagesByChatGroup_ChatGroupId(chatGroupId);
            response.setList(list);
            response.setMessage(AppConstants.GET_ALL_CHAT_MESSAGES_SUCCESS_MSG);
            response.setSuccess(true);
        } catch (Exception e) {
            response.setMessage(AppConstants.GET_ALL_CHAT_MESSAGES_FAILURE_MSG);
            response.setSuccess(false);
        }
        return response;
    }

    public ChatMessageSearchResponse retrieveChatMessageById(long chatMessageId) {
        ChatMessageSearchResponse response = new ChatMessageSearchResponse();
        ChatMessage chatMessage = chatMessageRepository.getReferenceById(chatMessageId);
        if (Objects.nonNull(chatMessage)) {
            response.setChatMessage(chatMessage);
            response.setMessage(AppConstants.GET_CHAT_MESSAGE_BY_ID_SUCCESS_MSG);
            response.setSuccess(true);
        } else {
            response.setMessage(AppConstants.GET_CHAT_GROUP_BY_ID_FAILURE_MSG);
            response.setSuccess(false);
        }

        return response;
    }

    public ChatMessageAddUpdateResponse createChatMessage(ChatMessageAddUpdateRequest request) {
        ChatMessageAddUpdateResponse response = new ChatMessageAddUpdateResponse();

        if (Objects.nonNull(request)) {
            ChatMessage chatMessage = new ChatMessage();

            // Map fields from request DTO to entity
            chatMessage.setSender(request.getSender());
            chatMessage.setContent(request.getContent());
            chatMessage.setSentDatetime(LocalDateTime.now());

            // Fetch the ChatGroup entity based on the chatGroupId in the request
            ChatGroup chatGroup = chatGroupRepository.findById(request.getChatGroupId()).orElseGet(ChatGroup::new);

            if (Objects.nonNull(chatGroup)) {
                chatMessage.setChatGroup(chatGroup);
            } else {
                // Handle case when chat group is not found
                response.setMessage("Chat group not found for ID: " + request.getChatGroupId());
                response.setSuccess(false);
                return response;
            }

            // Save the chat message to the database
            ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);

            // Set response with the saved chat message
            response.setChatMessage(savedChatMessage);
            response.setMessage(AppConstants.CREATE_UPDATE_CHAT_MESSAGE_SUCCESS_MSG);
            response.setSuccess(true);
        } else {
            response.setMessage(AppConstants.CREATE_UPDATE_CHAT_MESSAGE_FAILURE_MSG);
            response.setSuccess(false);
        }

        return response;
    }


    public ChatMessageAddUpdateResponse updateChatMessage(ChatMessageAddUpdateRequest request, long chatMessageId) {
        ChatMessageAddUpdateResponse response = new ChatMessageAddUpdateResponse();
        ChatMessage chatMessage = retrieveChatMessageById(chatMessageId).getChatMessage();
        ChatGroup chatGroup = new ChatGroup();

        if (Objects.nonNull(chatMessage)) {
            chatMessage.setContent(request.getContent());
            chatMessage.setSender(request.getSender());

            if (request.getChatGroupId() != null) {
                chatGroup = chatGroupRepository.findById(request.getChatGroupId()).orElseGet(ChatGroup::new);
                chatMessage.setChatGroup(chatGroup);
            }

            ChatMessage updatedChatMessage = chatMessageRepository.save(chatMessage);
            if (Objects.nonNull(updatedChatMessage)) {
                response.setChatMessage(updatedChatMessage);
                response.setSuccess(true);
                response.setMessage(AppConstants.CREATE_UPDATE_CHAT_MESSAGE_SUCCESS_MSG);
            } else {
                response.setMessage(AppConstants.CREATE_UPDATE_CHAT_MESSAGE_FAILURE_MSG);
            }
        }

        return response;
    }

    public ChatMessageSearchResponse deleteChatMessage(long chatMessageId) {
        ChatMessageSearchResponse response = new ChatMessageSearchResponse();
        ChatMessage chatMessage = retrieveChatMessageById(chatMessageId).getChatMessage();
        if (Objects.nonNull(chatMessage)) {
            chatMessageRepository.delete(chatMessage);
            response.setMessage(AppConstants.DELETE_CHAT_MESSAGE_SUCCESS_MSG);
            response.setSuccess(false);
        } else {
            response.setMessage(AppConstants.DELETE_CHAT_MESSAGE_FAILURE_MSG);
            response.setSuccess(false);
        }

        return response;
    }

    public String getUsernameInfo(String username) {
        return "Username: " + username;
    }
}
