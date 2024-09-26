package com.f2d.chatroom.service;

import com.f2d.chatroom.domain.*;
import com.f2d.chatroom.repository.F2DChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class F2DChatMessageService {

    @Autowired
    private F2DChatMessageRepository chatMessageRepository;

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

    public ChatMessageListResponse retrieveChatMessagesByGroup(ChatGroup chatGroup) {
        ChatMessageListResponse response = new ChatMessageListResponse();
        try {
            List<ChatMessage> list = chatMessageRepository.findChatMessagesByChatGroup(chatGroup);
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
            chatMessage.setSentDatetime(request.getSentDatetime());

//            // Fetch the ChatGroup entity based on the chatGroupId in the request
//            Optional<ChatGroup> chatGroupOpt = chatGroupRepository.findById(request.getChatGroupId());
//            if (chatGroupOpt.isPresent()) {
//                chatMessage.setChatGroup(chatGroupOpt.get());
//            } else {
//                response.setMessage(AppConstants.CHAT_GROUP_NOT_FOUND_MSG);
//                response.setSuccess(false);
//                return response; // Exit early if chat group is not found
//            }

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

        if (Objects.nonNull(chatMessage)) {
            chatMessage.setContent(request.getContent());
            chatMessage.setSender(request.getSender());

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
            response.setChatMessage(chatMessage);
            chatMessageRepository.delete(chatMessage);

            response.setMessage(AppConstants.DELETE_CHAT_MESSAGE_SUCCESS_MSG);
            response.setSuccess(false);
        } else {
            response.setMessage(AppConstants.DELETE_CHAT_MESSAGE_FAILURE_MSG);
            response.setSuccess(false);
        }

        return response;
    }
}
