package com.f2d.chatroom.domain;

public class UriConstants {
    public static final String GET_ALL_CHAT_GROUPS_URI = "/chatroom/chat-groups/all";
    public static final String GET_CHAT_GROUP_BY_ID_URI = "/chatroom/chat-groups/search/id/{chatGroupId}";
    public static final String CREATE_CHAT_GROUP_URI = "/chatroom/chat-groups/create";
    public static final String UPDATE_CHAT_GROUP_URI = "/chatroom/chat-groups/update/{chatGroupId}";
    public static final String DELETE_CHAT_GROUP_URI = "/chatroom/chat-groups/delete/{chatGroupId}";
    public static final String GET_ALL_CHAT_MESSAGES_URI = "/chatroom/chat-messages/all";
    public static final String GET_ALL_CHAT_MESSAGES_BY_GROUP_URI = "/chatroom/chat-messages/all/group/{chatGroupId}";
    public static final String GET_CHAT_MESSAGE_BY_ID_URI = "/chatroom/chat-messages/search/id/{chatMessageId}";
    public static final String CREATE_CHAT_MESSAGE_URI = "/chatroom/chat-messages/create";
    public static final String UPDATE_CHAT_MESSAGE_URI = "/chatroom/chat-messages/update/{chatMessageId}";
    public static final String DELETE_CHAT_MESSAGE_URI = "/chatroom/chat-messages/delete/{chatMessageId}";
    public static final String GET_F2D_GROUP_BY_ID_RELATIVE_PATH = "/groups/search/id/{groupId}";
    public static final String RETRIEVE_USERNAME_FROM_TOKEN_URI = "/chatroom/retrieve-username";
    public static final String GET_USER_BY_USERNAME = "/users/search/username/{username}";
}
