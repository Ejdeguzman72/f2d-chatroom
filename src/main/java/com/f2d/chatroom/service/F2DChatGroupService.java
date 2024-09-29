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
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
            List<ChatGroup> list = chatGroupRepository.findAll();
            response.setList(list);
            response.setMessage(AppConstants.GET_ALL_CHAT_GROUPS_SUCCESS_MSG);
            response.setSuccess(true);
        } catch (Exception e) {
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
                LOGGER.info("Retrieving f2d-group with ID: " + groupId);
                return response.getBody();
            }
        } catch (Exception e) {
            LOGGER.error("Error hitting f2d-group-builder service: " + e.getMessage());
        }

        return response.getBody();
    }

    public ChatGroupAddUpdateResponse createChatGroup(ChatGroupAddUpdateRequest request) {
        ChatGroupAddUpdateResponse response = new ChatGroupAddUpdateResponse();

        // Mapping the request to the ChatGroup entity
        ChatGroup chatGroup = new ChatGroup();
        chatGroup.setGroupName(request.getGroupName());
        chatGroup.setCreateDate(LocalDate.now());
        chatGroup.setLastUpdateTime(LocalDate.now());

        // Retrieve the F2DGroup and check for null
        F2DGroupSearchResponse f2dGroupSearchResponse = retrieveF2DGroup(request.getChatGroupId());
        F2DGroup f2dGroup = f2dGroupSearchResponse.getF2dGroup();
        LOGGER.info("f2dGroup: " + f2dGroup.toString());

//        if (f2dGroup == null) {
//            LOGGER.error("F2DGroup not found for chatGroupId: " + request.getChatGroupId());
//            response.setMessage("F2DGroup not found.");
//            response.setSuccess(false);
//            return response;
//        }
//
//        // Ensure that all required fields are set before saving
//        if (f2dGroup.getGroupName() == null) {
//            LOGGER.error("F2DGroup groupName cannot be null.");
//            response.setMessage("F2DGroup groupName cannot be null.");
//            response.setSuccess(false);
//            return response;
//        }

        chatGroup.setF2dGroup(f2dGroup); // Set the managed F2DGroup

        try {
            chatGroup = chatGroupRepository.save(chatGroup);

            // Check if the chat group was saved successfully
            if (Objects.nonNull(chatGroup.getChatGroupId())) {
                response.setChatGroup(chatGroup);
                response.setMessage(AppConstants.CREATE_CHAT_GROUP_SUCCESS_MSG);
                response.setSuccess(true);
                LOGGER.info("Successfully created chat group with ID: " + chatGroup.getChatGroupId());
            } else {
                response.setMessage(AppConstants.CREATE_CHART_GROUP_FAILURE_MSG);
                response.setSuccess(false);
            }
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
            chatMessage.setF2dGroup(f2DGroup.getBody().getF2dGroup());
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
            chatGroupRepository.deleteById(chatGroupId);
            response.setChatGroup(chatGroup);
            response.setMessage(AppConstants.DELETE_CHAT_GROUP_SUCCESS_MSG);
            response.setSuccess(true);
        } else {
            response.setMessage(AppConstants.DELETE_CHAT_GROUP_FAILURE_MSG);
            response.setSuccess(false);
        }

        return response;
    }
}
