package com.ansysan.project.authentication_service.service;

import com.ansysan.project.authentication_service.dto.LoginRequest;
import com.ansysan.project.authentication_service.dto.RegistrationRequest;
import com.ansysan.project.authentication_service.dto.TokenResponse;
import com.ansysan.project.authentication_service.dto.UserDto;

public interface AuthenticationService {

    TokenResponse login(LoginRequest loginRequest);

    UserDto registration (RegistrationRequest registrationRequest);

    TokenResponse refreshToken(String refreshToken);
}