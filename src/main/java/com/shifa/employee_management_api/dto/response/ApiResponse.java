package com.shifa.employee_management_api.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private boolean success;
    private int statusCode;
    private String message;
    private T data;
    private LocalDateTime timestamp;
}
