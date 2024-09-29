package com.f2d.chatroom.controller;

import com.f2d.chatroom.domain.*;
import com.f2d.chatroom.service.F2DChatGroupService;
import jakarta.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin
public class ChatGroupController {

    @Autowired
    private F2DChatGroupService f2dChatGroupService;

    @GetMapping(UriConstants.GET_ALL_CHAT_GROUPS_URI)
    public ResponseEntity<ChatGroupListResponse> retrieveAllChatGroupInformation() {
        ChatGroupListResponse response = f2dChatGroupService.retrieveAllChatGroupInfo();
        return ResponseEntity.ok(response);
    }

    @GetMapping(UriConstants.GET_CHAT_GROUP_BY_ID_URI)
    public ResponseEntity<ChatGroupSearchResponse> retrieveChatGroupById(@PathVariable UUID chatGroupId) {
        ChatGroupSearchResponse response = f2dChatGroupService.retrieveChatGroupById(chatGroupId);
        return ResponseEntity.ok(response);
    }

    @PostMapping(UriConstants.CREATE_CHAT_GROUP_URI)
    public ResponseEntity<ChatGroupAddUpdateResponse> createChatGroup(@RequestBody ChatGroupAddUpdateRequest request) {
        ChatGroupAddUpdateResponse response = f2dChatGroupService.createChatGroup(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping(UriConstants.GET_F2D_GROUP_BY_ID_RELATIVE_PATH)
    public F2DGroupSearchResponse retrieveGroupById(@PathVariable UUID groupId) {
        return f2dChatGroupService.retrieveF2DGroup(groupId);
    }

    @PutMapping(UriConstants.UPDATE_CHAT_GROUP_URI)
    public ResponseEntity<ChatGroupAddUpdateResponse> updateChatGroup(@RequestBody ChatGroupAddUpdateRequest request, @PathVariable UUID chatGroupId) {
        ChatGroupAddUpdateResponse response = f2dChatGroupService.updateChatGroup(request,chatGroupId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(UriConstants.DELETE_CHAT_GROUP_URI)
    public ResponseEntity<ChatGroupSearchResponse> deleteChatGroup(@PathVariable UUID chatGroupId) {
        ChatGroupSearchResponse response = f2dChatGroupService.deleteChatGroup(chatGroupId);
        return ResponseEntity.ok(response);
    }
}
