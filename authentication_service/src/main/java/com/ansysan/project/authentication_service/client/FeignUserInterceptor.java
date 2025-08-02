package com.ansysan.project.authentication_service.client;

import com.ansysan.project.authentication_service.config.UserContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FeignUserInterceptor implements RequestInterceptor {

    private final UserContext userContext;

    @Override
    public void apply(RequestTemplate template) {
        template.header("auth-user-id", String.valueOf(userContext.getUserId()));
    }
}
