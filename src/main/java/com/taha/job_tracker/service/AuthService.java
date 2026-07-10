package com.taha.job_tracker.service;

import com.taha.job_tracker.dto.*;
import com.taha.job_tracker.entity.RefreshToken;
import com.taha.job_tracker.entity.Role;
import com.taha.job_tracker.entity.User;
import com.taha.job_tracker.exception.BusinessRuleException;
import com.taha.job_tracker.exception.DuplicateResourceException;
import com.taha.job_tracker.repository.UserRepository;
import com.taha.job_tracker.security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request, HttpServletResponse response) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already in use");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(Role.USER)
                .build();

        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = jwtService.generateRefreshToken(user);

        setRefreshTokenCookie(response, refreshToken.getToken());

        return buildAuthResponse(accessToken, user);
    }

    @Transactional
    public AuthResponse login(LoginRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessRuleException("User not found"));

        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = jwtService.generateRefreshToken(user);

        setRefreshTokenCookie(response, refreshToken.getToken());

        return buildAuthResponse(accessToken, user);
    }

    @Transactional
    public AuthResponse refresh(HttpServletRequest request, HttpServletResponse response) {
        String oldToken = extractRefreshTokenFromCookie(request);

        RefreshToken newRefreshToken = jwtService.rotateRefreshToken(oldToken);
        String newAccessToken = jwtService.generateAccessToken(newRefreshToken.getUser());

        setRefreshTokenCookie(response, newRefreshToken.getToken());

        return buildAuthResponse(newAccessToken, newRefreshToken.getUser());
    }

    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String token = extractRefreshTokenFromCookie(request);

        jwtService.rotateRefreshToken(token);

        Cookie cookie = new Cookie("refresh_token", null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("refresh_token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // set to true in prod
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            throw new BusinessRuleException("No refresh token found");
        }
        return Arrays.stream(request.getCookies())
                .filter(c -> "refresh_token".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new BusinessRuleException("No refresh token found"));
    }

    private AuthResponse buildAuthResponse(String accessToken, User user) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .user(UserResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .role(user.getRole())
                        .build())
                .build();
    }
}