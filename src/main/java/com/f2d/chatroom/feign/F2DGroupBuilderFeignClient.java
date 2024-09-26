package com.f2d.chatroom.feign;

import com.f2d.chatroom.config.FeignConfig;
import com.f2d.chatroom.domain.AppConstants;
import com.f2d.chatroom.domain.F2DGroupSearchResponse;
import com.f2d.chatroom.domain.UriConstants;
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
    @GetMapping(UriConstants.GET_F2D_GROUP_BY_ID_RELATIVE_PATH)
    ResponseEntity<F2DGroupSearchResponse> getGroupById(@PathVariable(AppConstants.GROUP_ID) UUID groupId);
}
