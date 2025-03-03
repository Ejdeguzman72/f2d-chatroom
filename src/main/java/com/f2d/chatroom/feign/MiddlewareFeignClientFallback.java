package com.f2d.chatroom.feign;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class MiddlewareFeignClientFallback implements MiddlewareFeignClient {
    @Override
    public ResponseEntity<String> getUserInfo(String username) {
        String response = "username not found";
        return ResponseEntity.ok(response);
    }
}
