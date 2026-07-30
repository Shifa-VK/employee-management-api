package com.shifa.employee_management_api.service.impl;

import com.shifa.employee_management_api.dto.request.LoginRequest;
import com.shifa.employee_management_api.dto.request.RefreshTokenRequest;
import com.shifa.employee_management_api.dto.request.RegisterRequest;
import com.shifa.employee_management_api.dto.response.JwtResponse;
import com.shifa.employee_management_api.dto.response.RefreshTokenResponse;
import com.shifa.employee_management_api.dto.response.RegisterResponse;
import com.shifa.employee_management_api.entity.User;
import com.shifa.employee_management_api.exception.DuplicateEmailException;
import com.shifa.employee_management_api.repository.UserRepository;
import com.shifa.employee_management_api.security.CustomUserDetails;
import com.shifa.employee_management_api.security.CustomUserDetailsService;
import com.shifa.employee_management_api.security.JwtService;
import com.shifa.employee_management_api.service.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    @Value("${jwt.access-token.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;
    @Override
    public RegisterResponse register(RegisterRequest request) {
        log.warn("Registering user");
        if (userRepository.existsByEmail(request.getEmail())){
            log.error("Employee already exists with email: {} ", request.getEmail());
            throw new DuplicateEmailException("Email already exists");
        }
        User user = User.builder()
                .name(request.getName())
                .email((request.getEmail()))
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        log.info("Registration completed successfully");
        User savedUser = userRepository.save(user);
        return RegisterResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .build();
    }

    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        log.info("Authenticating user: {}", loginRequest.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()->{
                    log.warn("User not found with email: {}", loginRequest.getEmail());
                    throw new UsernameNotFoundException("User not found with email: " + loginRequest.getEmail());
                });
        log.info("User logged in successfully: {}", user.getEmail());
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getEmail());
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(accessTokenExpiration)
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        try {

            String refreshToken = request.getRefreshToken();

            // Verify token type
            if (!"REFRESH".equals(jwtService.extractTokenType(refreshToken))) {
                throw new BadCredentialsException("Invalid refresh token");
            }

            String username = jwtService.extractUsername(refreshToken);

            UserDetails userDetails =
                    customUserDetailsService.loadUserByUsername(username);

            if (!jwtService.isTokenValid(refreshToken, userDetails)) {
                throw new BadCredentialsException("Refresh token is invalid or expired");
            }

            String accessToken =
                    jwtService.generateAccessToken(userDetails);

            return RefreshTokenResponse.builder()
                    .accessToken(accessToken)
                    .expiresIn(jwtService.getAccessTokenExpiration())
                    .build();
        } catch (
                ExpiredJwtException ex) {
            throw new BadCredentialsException("Refresh token has expired. Please login again.");
        }
    }
}
