package com.ansysan.project.notification_service.client;

import com.ansysan.project.notification_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "authentication-service", url = "${authentication-service.host}:${authentication-service.port}")
public interface AuthenticationServiceClient {

    @GetMapping("/api/v1/users/{id}")
    UserDto getUser(@PathVariable long id);

}
