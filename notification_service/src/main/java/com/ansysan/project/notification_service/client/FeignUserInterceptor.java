package com.ansysan.project.notification_service.client;

import com.ansysan.project.notification_service.config.context.UserContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FeignUserInterceptor implements RequestInterceptor {

    private final UserContext userContext;

    @Override
    public void apply(RequestTemplate template) {
        template.header("notification-id", String.valueOf(userContext.getUserId()));
    }
}
