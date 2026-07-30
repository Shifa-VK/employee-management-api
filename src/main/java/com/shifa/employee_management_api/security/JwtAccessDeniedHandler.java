package com.shifa.employee_management_api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shifa.employee_management_api.dto.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Author: Shifa VK
 * Created: 23-07-2026
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ApiResponse<Object> apiResponse = new ApiResponse<>(
                false,
                HttpStatus.FORBIDDEN.value(),
                "Access Denied. You don't have permission to perform this action.",
                null,
                LocalDateTime.now()
        );

        new ObjectMapper().writeValue(response.getOutputStream(), apiResponse);
    }
}