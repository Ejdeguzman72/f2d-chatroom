package com.f2d.chatroom.feign;

import com.f2d.chatroom.domain.AppConstants;
import com.f2d.chatroom.domain.F2DGroupSearchResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class F2DGroupBuilderClientFallback implements F2DGroupBuilderFeignClient {

    @Override
    public ResponseEntity<F2DGroupSearchResponse> retrieveGroupById(UUID groupId) {
        F2DGroupSearchResponse response = new F2DGroupSearchResponse();
        response.setMessage(AppConstants.F2D_GROUP_BUILDER_CALL_FAILURE);
        response.setSuccess(false);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<F2DGroupSearchResponse> retrieveGroupByGroupName(String groupName) {
        F2DGroupSearchResponse response = new F2DGroupSearchResponse();
        response.setMessage(AppConstants.F2D_GROUP_BUILDER_CALL_FAILURE);
        response.setSuccess(false);
        return ResponseEntity.ok(response);
    }
}