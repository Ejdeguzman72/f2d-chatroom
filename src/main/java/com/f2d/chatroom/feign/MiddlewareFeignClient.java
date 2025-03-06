package com.f2d.chatroom.feign;

import com.f2d.chatroom.config.FeignConfig;
import com.f2d.chatroom.domain.AppConstants;
import com.f2d.chatroom.domain.UriConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = AppConstants.F2D_MIDDLEWARE,
        url = AppConstants.LOCALHOST,
        configuration = FeignConfig.class,
        fallback = F2DGroupBuilderClientFallback.class)
public interface MiddlewareFeignClient {

    @GetMapping(UriConstants.RETRIEVE_USERNAME_FROM_TOKEN_URI)
    ResponseEntity<String> getUserInfo(@RequestHeader("X-User-Name") String username);
}
