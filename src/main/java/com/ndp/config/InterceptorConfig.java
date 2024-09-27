package com.ndp.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class InterceptorConfig implements HandlerInterceptor {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    private static final String CORRELATION_ID_LOG = "correlationId";

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        String corId = UUID.randomUUID().toString();
        MDC.put(CORRELATION_ID_LOG, corId);
        response.setHeader(CORRELATION_ID_HEADER, corId);
        return true;
    }

    @Override
    public void afterCompletion(final @NonNull HttpServletRequest request,
                                final @NonNull HttpServletResponse response,
                                final @NonNull Object handler,
                                final Exception ex) {
        MDC.remove(CORRELATION_ID_LOG);
    }
}
