package com.f2d.chatroom.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@CrossOrigin
public class F2DGroupListResponse {

    List<F2DGroup> list;
    String message;
    boolean isSuccess;

    public List<F2DGroup> getList() {
        return list;
    }
    public void setList(List<F2DGroup> list) {
        this.list = list;
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
