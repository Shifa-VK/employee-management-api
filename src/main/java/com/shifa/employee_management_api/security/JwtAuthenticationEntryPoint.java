package com.shifa.employee_management_api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shifa.employee_management_api.dto.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Author: Shifa VK
 * Created: 23-07-2026
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {
        log.error("AuthenticationEntryPoint called");
        log.error("URI: {}", request.getRequestURI());
        log.error("Exception: {}", authException.getClass().getName());
        log.error("Message: {}", authException.getMessage());
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ApiResponse<Object> apiResponse = new ApiResponse<>(
                false,
                HttpStatus.UNAUTHORIZED.value(),
                "Authentication failed. Please login again.",
                null,
                LocalDateTime.now()
        );

        new ObjectMapper().writeValue(response.getOutputStream(), apiResponse);
    }
}
