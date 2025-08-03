package com.ansysan.project.authentication_service.service.authentication;

import com.ansysan.project.authentication_service.dto.request.LoginRequest;
import com.ansysan.project.authentication_service.dto.request.RegistrationRequest;
import com.ansysan.project.authentication_service.dto.response.TokenResponse;
import com.ansysan.project.authentication_service.dto.response.UserResponse;

public interface AuthenticationService {

    TokenResponse login(LoginRequest loginRequest);

    UserResponse registration (RegistrationRequest registrationRequest);

    TokenResponse refreshToken(String refreshToken);
}