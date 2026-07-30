package com.shifa.employee_management_api.controller;

import com.shifa.employee_management_api.dto.request.LoginRequest;
import com.shifa.employee_management_api.dto.request.RefreshTokenRequest;
import com.shifa.employee_management_api.dto.request.RegisterRequest;
import com.shifa.employee_management_api.dto.response.ApiResponse;
import com.shifa.employee_management_api.dto.response.JwtResponse;
import com.shifa.employee_management_api.dto.response.RefreshTokenResponse;
import com.shifa.employee_management_api.dto.response.RegisterResponse;
import com.shifa.employee_management_api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
@Slf4j
@Tag(
        name = "Authentication",
        description = "APIs for user registration, login, JWT authentication, and authorization"
)
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /*Register */
    @Operation(
            summary = "Register User",
            security = {}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Registration Successfull"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Email already exists"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation Failed"
            )
    }
    )
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> registerUser(@Valid @RequestBody RegisterRequest request){
        RegisterResponse response = authService.register(request);
        ApiResponse<RegisterResponse> apiResponse = new ApiResponse<>(
                true,
                HttpStatus.CREATED.value(),
                "Registration Successfull",
                response,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(
            @Valid @RequestBody LoginRequest request
            ){
        JwtResponse jwtResponse = authService.login(request);
        ApiResponse<JwtResponse> apiResponse = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Login Successful",
                jwtResponse,
                LocalDateTime.now()
        );
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/refresh-token")
    @Operation(
            summary = "Refresh Access Token",
            security = {}
    )
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {
        log.info("Refresh token controller reached");
        RefreshTokenResponse response =
                authService.refreshToken(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        HttpStatus.OK.value(),
                        "Access token generated successfully",
                        response,
                        LocalDateTime.now()
                )
        );
    }
}
