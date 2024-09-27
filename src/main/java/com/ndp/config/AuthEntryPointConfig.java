package com.ndp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndp.model.dto.response.ResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class AuthEntryPointConfig implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        log.error("Error authentication: {}", authException.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errBody(authException));
        response.getOutputStream().flush();
    }

    private ResponseDto errBody(AuthenticationException authenticationException) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return new ResponseDto(status.value(), status.getReasonPhrase(), errObject(status, authenticationException));
    }

    private Map<String, Object> errObject(HttpStatus status, AuthenticationException authenticationException) {
        Map<String, Object> result = new HashMap<>();
        result.put("timestamp", new Date());
        result.put("status", status.value());
        result.put("error", authenticationException.getMessage());
        return result;
    }
}
