package com.f2d.chatroom.service;

import com.f2d.chatroom.domain.*;
import com.f2d.chatroom.feign.F2DGroupBuilderFeignClient;
import com.f2d.chatroom.repository.F2DChatGroupRepository;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.GroupPrincipal;
import java.time.LocalDate;
import java.util.*;

@Service
public class F2DChatGroupService {

    @Autowired
    private F2DChatGroupRepository chatGroupRepository;
    @Autowired
    private F2DGroupBuilderFeignClient f2dGroupBuilderClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(F2DChatGroupService.class);

    public ChatGroupListResponse retrieveAllChatGroupInfo() {
        ChatGroupListResponse response = new ChatGroupListResponse();
        try {
            // Retrieve all ChatGroup entities from the repository
            List<ChatGroup> list = chatGroupRepository.findAll();

            // Set the response with the valid ChatGroup list
            response.setList(list);
            response.setMessage(AppConstants.GET_ALL_CHAT_GROUPS_SUCCESS_MSG);
            response.setSuccess(true);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            response.setMessage(AppConstants.GET_ALL_CHAT_GROUPS_FAILURE_MSG);
            response.setSuccess(false);
        }

        return response;
    }

    public ChatGroupSearchResponse retrieveChatGroupById(UUID chatGroupId) {
        ChatGroupSearchResponse response = new ChatGroupSearchResponse();
        try {
            ChatGroup chatGroup = chatGroupRepository.findById(chatGroupId).orElseGet(ChatGroup::new);
            response.setChatGroup(chatGroup);
            LOGGER.info("Retrieving chat group with ID: " + chatGroupId);
            response.setMessage(AppConstants.GET_CHAT_GROUP_BY_ID_SUCCESS_MSG);
            response.setSuccess(true);
        } catch (Exception e) {
            response.setMessage(AppConstants.GET_CHAT_GROUP_BY_ID_FAILURE_MSG);
            response.setSuccess(false);
        }

        return response;
    }
    public F2DGroupSearchResponse retrieveF2DGroup(UUID groupId) {
        ResponseEntity<F2DGroupSearchResponse> response = null;
        try {
            response = f2dGroupBuilderClient.retrieveGroupById(groupId);
            if (response.getStatusCode() == HttpStatusCode.valueOf(HttpStatus.SC_OK)) {
                LOGGER.info("Retrieving f2d-group with groupId: " + groupId);
                LOGGER.info(response.getBody().getF2dGroup().getGroupId().toString());
                return response.getBody();
            }
        } catch (Exception e) {
            LOGGER.error("Error hitting f2d-group-builder service: " + e.getMessage());
        }

        return response.getBody();
    }

    public boolean checkForDuplicateChatGroupNames(String chatGroupName) {
        List<String> chatGroupNameList = chatGroupRepository.findAll()
                .stream()
                .map(ChatGroup::getGroupName)
                .toList();
        Set<String> set = new HashSet<>();
        for (String name : chatGroupNameList) {
            if (chatGroupNameList.contains(chatGroupName)) {
                return true;
            }
        }

        return false;
    }

    public ChatGroupAddUpdateResponse createChatGroup(ChatGroupAddUpdateRequest request) {
        ChatGroupAddUpdateResponse response = new ChatGroupAddUpdateResponse();
        try {
            ChatGroup chatGroup = new ChatGroup();
            if (checkForDuplicateChatGroupNames(request.getGroupName())) {
                response.setMessage(AppConstants.DUPLICATE_ENTRY);
                response.setSuccess(false);
                return response;
            } else {
                chatGroup.setGroupName(request.getGroupName());
                chatGroup.setCreateDate(LocalDate.now());
                chatGroup.setLastUpdateTime(LocalDate.now());
                chatGroupRepository.save(chatGroup);
            }
            response.setChatGroup(chatGroup);
            response.setMessage(AppConstants.CREATE_CHAT_GROUP_SUCCESS_MSG);
            response.setSuccess(true);
            LOGGER.info("Successfully created chat group with ID: " + chatGroup.getGroupName());
        } catch (Exception e) {
            LOGGER.error("Error occurred while creating chat group: ", e);
            response.setSuccess(false);
            response.setMessage("An exception occurred while saving the chat group: " + e.getMessage());
        }

        return response;
    }

    public ChatGroupAddUpdateResponse updateChatGroup(ChatGroupAddUpdateRequest request, UUID chatMessageId) {
        ChatGroupAddUpdateResponse response = new ChatGroupAddUpdateResponse();
        ChatGroup chatMessage = retrieveChatGroupById(chatMessageId).getChatGroup();

        if (Objects.nonNull(chatMessage)) {
            chatMessage.setChatGroupId(request.getChatGroupId());
            ResponseEntity<F2DGroupSearchResponse> f2DGroup = f2dGroupBuilderClient.retrieveGroupById(request.getGroupId());
            chatMessage.setGroupName(request.getGroupName());
            chatMessage.setCreateDate(request.getCreateDate());
            chatMessage.setLastUpdateTime(request.getLastUpdateTime());

            ChatGroup updatedChatgroup = chatGroupRepository.save(chatMessage);
            if (Objects.nonNull(updatedChatgroup.getChatGroupId())) {
                response.setSuccess(true);
                response.setMessage(AppConstants.UPDATE_CHAT_GROUP_SUCCESS_MSG);

                LOGGER.info("Updating chat group: " + chatMessageId);
            } else {
                response.setMessage(AppConstants.UPDATE_CHAT_GROUP_FAILURE_MSG);
                response.setSuccess(false);
            }

            response.setChatGroup(chatMessage);
        }

        return response;
    }

    public ChatGroupSearchResponse deleteChatGroup(UUID chatGroupId) {
        ChatGroupSearchResponse response = new ChatGroupSearchResponse();
        ChatGroup chatGroup = retrieveChatGroupById(chatGroupId).getChatGroup();
        if (Objects.nonNull(chatGroup)) {
            try {
                chatGroupRepository.deleteById(chatGroupId);
                response.setChatGroup(chatGroup);
                response.setMessage(AppConstants.DELETE_CHAT_GROUP_SUCCESS_MSG);
                response.setSuccess(true);
            } catch (Exception e) {
                response.setSuccess(false);
                response.setMessage(AppConstants.DELETE_CHAT_GROUP_FAILURE_MSG + e.toString());
            }
        }

        return response;
    }
}
