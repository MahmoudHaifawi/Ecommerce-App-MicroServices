package com.micro.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignAuthConfig {

    @Bean
    public RequestInterceptor authHeaderForwardingInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // Get current HTTP request (the original call from Postman)
                ServletRequestAttributes attrs =
                        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

                if (attrs == null) {
                    // No HTTP context (async, scheduler, etc.) → nothing to forward
                    return;
                }

                HttpServletRequest request = attrs.getRequest();
                String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

                // If the incoming request had Authorization, forward it
                if (authHeader != null && !authHeader.isBlank()) {
                    template.header(HttpHeaders.AUTHORIZATION, authHeader);
                }
            }
        };
    }
}
