package com.f2d.chatroom.domain;

public class UriConstants {
    public static final String GET_ALL_CHAT_GROUPS_URI = "/chat-groups/all";
    public static final String GET_CHAT_GROUP_BY_ID_URI = "/chat-groups/search/id/{chatGroupId}";
    public static final String CREATE_CHAT_GROUP_URI = "/chat-groups/create";
    public static final String UPDATE_CHAT_GROUP_URI = "/chat-groups/update/{chatGroupId}";
    public static final String DELETE_CHAT_GROUP_URI = "/chat-groups/delete/{chatGroupId}";
    public static final String GET_ALL_CHAT_MESSAGES_URI = "/chat-messages/all";
    public static final String GET_ALL_CHAT_MESSAGES_BY_GROUP_URI = "/chat-messages/all/group/{groupId}";
    public static final String GET_CHAT_MESSAGE_BY_ID_URI = "/chat-messages/search/id/{chatMessageId}";
    public static final String CREATE_CHAT_MESSAGE_URI = "/chat-messages/create";
    public static final String UPDATE_CHAT_MESSAGE_URI = "/chat-messages/update/{chatMessageId}";
    public static final String DELETE_CHAT_MESSAGE_URI = "/chat-messages/delete/{chatMessageId}";
}
