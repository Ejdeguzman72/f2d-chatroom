package com.f2d.chatroom.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Component;

@Component
@EnableFeignClients(basePackages = "com.f2d.chatroom.feign")
public class FeignConfig {
}