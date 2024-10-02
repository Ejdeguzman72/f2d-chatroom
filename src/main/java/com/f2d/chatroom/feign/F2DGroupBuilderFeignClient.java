package com.f2d.chatroom.feign;

import com.f2d.chatroom.config.FeignConfig;
import com.f2d.chatroom.domain.AppConstants;
import com.f2d.chatroom.domain.F2DGroupListResponse;
import com.f2d.chatroom.domain.F2DGroupSearchResponse;
import jakarta.ws.rs.Path;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = AppConstants.F2D_GROUP_BUILDER,
        url = AppConstants.LOCALHOST,
        configuration = FeignConfig.class,
        fallback = F2DGroupBuilderClientFallback.class)
public interface F2DGroupBuilderFeignClient {

    @GetMapping("/groups/all")
    ResponseEntity<F2DGroupListResponse> retrieveAllGroups();
    @GetMapping("/groups/search/id/{groupId}")
    ResponseEntity<F2DGroupSearchResponse> retrieveGroupById(@PathVariable("groupId") UUID groupId);

    @GetMapping("/groups/search/groupName/{groupName}")
    ResponseEntity<F2DGroupSearchResponse> retrieveGroupByGroupName(@PathVariable("groupName") String groupName);
}
