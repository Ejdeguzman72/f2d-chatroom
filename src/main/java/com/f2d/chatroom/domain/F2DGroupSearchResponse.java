package com.f2d.chatroom.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.web.bind.annotation.CrossOrigin;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@CrossOrigin
public class F2DGroupSearchResponse {

    F2DGroup f2dGroup;
    String message;
    boolean isSuccess;

    public F2DGroup getF2dGroup() {
        return f2dGroup;
    }
    public void setF2dGroup(F2DGroup f2dGroup) {
        this.f2dGroup = f2dGroup;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public boolean isSuccess() {
        return isSuccess;
    }
    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
