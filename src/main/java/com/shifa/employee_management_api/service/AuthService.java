package com.shifa.employee_management_api.service;

import com.shifa.employee_management_api.dto.request.LoginRequest;
import com.shifa.employee_management_api.dto.request.RefreshTokenRequest;
import com.shifa.employee_management_api.dto.request.RegisterRequest;
import com.shifa.employee_management_api.dto.response.JwtResponse;
import com.shifa.employee_management_api.dto.response.RefreshTokenResponse;
import com.shifa.employee_management_api.dto.response.RegisterResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    RegisterResponse register(RegisterRequest registerRequest);
    JwtResponse login(LoginRequest loginRequest);
    RefreshTokenResponse refreshToken(RefreshTokenRequest request);
}
