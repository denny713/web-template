package com.ndp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final InterceptorConfig interceptorConfig;

    public WebConfig(InterceptorConfig interceptorConfig) {
        this.interceptorConfig = interceptorConfig;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptorConfig);
    }
}
