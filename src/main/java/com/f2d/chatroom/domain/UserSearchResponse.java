package com.f2d.chatroom.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSearchResponse {

    F2DUser user;

    public F2DUser getUser() {
        return user;
    }

    public void setUser(F2DUser user) {
        this.user = user;
    }
}
