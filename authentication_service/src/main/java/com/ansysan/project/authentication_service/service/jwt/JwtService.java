package com.ansysan.project.authentication_service.service.jwt;

import com.ansysan.project.authentication_service.dto.response.TokenResponse;
import com.ansysan.project.authentication_service.entity.User;

public interface JwtService {

    TokenResponse generateToken(User user);

    TokenResponse regenerateToken(String token);
}