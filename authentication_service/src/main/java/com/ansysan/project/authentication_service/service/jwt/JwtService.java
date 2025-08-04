package com.ansysan.project.authentication_service.service.jwt;

import com.ansysan.project.authentication_service.dto.response.TokenResponse;
import com.ansysan.project.authentication_service.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface JwtService {

    TokenResponse generateToken(UserDetails user);

    TokenResponse regenerateToken(String token);
}