package com.f2d.chatroom.feign;

import com.f2d.chatroom.domain.UserSearchResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class F2DUserAuthClientFallback implements F2DUserAuthClient {
    @Override
    public ResponseEntity<UserSearchResponse> retrieveUserByUsername(String username) {
        UserSearchResponse response = new UserSearchResponse();
        response.setUser(null);
        return ResponseEntity.ok(response);
    }
}
